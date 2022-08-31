package com.sxjkwm.pm.business.eplatform.service;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dao.EsDao;
import com.sxjkwm.pm.business.eplatform.dto.InboundInvoiceDto;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/8/30 9:38
 */
@Service
public class InboundInvoiceServiceV2 extends EpBaseService<InboundInvoiceDto> {

    private static final Logger logger = LoggerFactory.getLogger(InboundInvoiceService.class);

    static final String inboundInvoiceIndex = "pepiiv2";

    private static final ConditionPair[] invoiceBillWorkbookHeaders = {
            ConditionPair.of("supplierName", "供应商名称", String.class),
            ConditionPair.of("buyOrderNo", "平台订单号", Long.class),
            ConditionPair.of("finalPrice", "结算金额", BigDecimal.class),
            ConditionPair.of("buyInvoiceAmount", "发票金额", BigDecimal.class),
            ConditionPair.of("buyerOrgName", "采购单位", String.class),
            ConditionPair.of("buyInvoiceApplyNumber", "发票申请单号", String.class),
            ConditionPair.of("invoiceNo", "发票号", String.class),
            ConditionPair.of("ownerName", "对接人", String.class),
            ConditionPair.of("hasOutbound", "是否已开销项", Boolean.class),
    };

    private final EpDao epDao;
    private final EsDao esDao;
    private final S3FileUtil s3FileUtil;

    @Autowired
    public InboundInvoiceServiceV2(EpDao epDao, EsDao esDao, S3FileUtil s3FileUtil) {
        super(epDao, esDao);
        this.epDao = epDao;
        this.esDao = esDao;
        this.s3FileUtil = s3FileUtil;
    }

    @Override
    protected InboundInvoiceDto convert(Map<String, Object> dataRow) {
        return InboundInvoiceDto.fillData(dataRow);
    }

    @Override
    protected String getSQL() {
        return EpSql.inboundInvoiceSql2;
    }

    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public Boolean syncData() {
        try {
            super.syncToEs(inboundInvoiceIndex, "buyOrderNo");
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.error("Sync inbound invoice data to ES failed: {}", e);
            return Boolean.FALSE;
        }
    }

    private void createRowHeader(XSSFSheet sheet, XSSFCellStyle dataStyle) {
        XSSFRow row = sheet.createRow(1);
        for (int i = 0; i < invoiceBillWorkbookHeaders.length; i ++) {
            ConditionPair conditionPair = invoiceBillWorkbookHeaders[i];
            XSSFCell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(conditionPair.colName);
            cell.setCellStyle(dataStyle);
        }
    }

    public PageDataDto<Map<String, Object>> queryDataInEs(Integer pageSize, Integer pageNo, Long startTime, Long endTime, String[] supplierNames, String buyInvoiceApplyNumber, String invoiceNo) throws IOException {
        BoolQueryBuilder queryCondition = QueryBuilders.boolQuery();
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("operateTime").from(startTime, true).to(endTime, true));
        } else if (Objects.nonNull(startTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("operateTime").from(startTime, true));
        } else if (Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("operateTime").to(endTime, true));
        }
        if (StringUtils.isNotBlank(buyInvoiceApplyNumber)) {
            queryCondition.must(QueryBuilders.matchQuery("buyInvoiceApplyNumber.keyword", buyInvoiceApplyNumber.trim()));
        }
        if (Objects.nonNull(supplierNames) && supplierNames.length > 0) {
            queryCondition.must(QueryBuilders.termsQuery("supplierName.keyword", supplierNames));
        }
        if (StringUtils.isNotBlank(invoiceNo)) {
            queryCondition.must(QueryBuilders.matchQuery("invoiceNo.keyword", invoiceNo));
        }
        return super.queryFromES(inboundInvoiceIndex, queryCondition, pageSize, pageNo, "invoiceNo");
    }

    public String generateWorkbook(List<InboundInvoiceDto> dataList) throws NoSuchFieldException, IllegalAccessException {
        // relate to outbound invoice data
        for (InboundInvoiceDto inbound: dataList) {
            Long outboundId = inbound.getSaleOrderNo();
            Boolean hasOutbound = false;
            try {
                Map<String,Object> resp = esDao.searchDataById(OutboundInvoiceService.outboundInvoiceIndex, outboundId.toString(), "saleInvoiceApplyNumber");
                if (Objects.nonNull(resp)) {
                    Object resObj = resp.get("saleInvoiceApplyNumber");
                    if (Objects.nonNull(resObj)) {
                        String num = resObj.toString();
                        if (StringUtils.isNotBlank(num)) {
                            hasOutbound = true;
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Search outbound data failed: {}", e);
            }
            inbound.setHasOutbound(hasOutbound);
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        Map<String, List<InboundInvoiceDto>> dataMapBySupplier = dataList.stream().collect(Collectors.groupingBy(InboundInvoiceDto::getSupplierName));
        Set<Map.Entry<String, List<InboundInvoiceDto>>> entrySet = dataMapBySupplier.entrySet();
        Iterator<Map.Entry<String, List<InboundInvoiceDto>>> iterator = entrySet.iterator();
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeight(18);
        titleFont.setFontName("等线");
        XSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont dataFont = workbook.createFont();
        dataFont.setFontHeight(11);
        dataFont.setFontName("等线");
        XSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFont(dataFont);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont resultFont = workbook.createFont();
        resultFont.setFontHeight(11);
        resultFont.setFontName("等线");
        resultFont.setBold(true);
        XSSFCellStyle resultStyle = workbook.createCellStyle();
        resultStyle.setFont(resultFont);
        resultStyle.setBorderBottom(BorderStyle.THIN);
        resultStyle.setBorderLeft(BorderStyle.THIN);
        resultStyle.setBorderRight(BorderStyle.THIN);
        resultStyle.setBorderTop(BorderStyle.THIN);
        resultStyle.setAlignment(HorizontalAlignment.CENTER);
        resultStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFFont errResultFont = workbook.createFont();
        errResultFont.setFontHeight(11);
        errResultFont.setFontName("等线");
        errResultFont.setBold(true);
        XSSFCellStyle errResultStyle = workbook.createCellStyle();
        errResultStyle.setFont(errResultFont);
        errResultStyle.setFillForegroundColor((short) 18);
        errResultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        errResultStyle.setBorderBottom(BorderStyle.THIN);
        errResultStyle.setBorderLeft(BorderStyle.THIN);
        errResultStyle.setBorderRight(BorderStyle.THIN);
        errResultStyle.setBorderTop(BorderStyle.THIN);
        errResultStyle.setAlignment(HorizontalAlignment.CENTER);
        errResultStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        while (iterator.hasNext()) {
            Map.Entry<String, List<InboundInvoiceDto>> entry = iterator.next();
            String sheetName = entry.getKey();
            processSheetBySupplierName(workbook, sheetName, entry.getValue(), titleStyle, dataStyle, resultStyle, errResultStyle);
        }
        String fileName = "进项发票订单明细表.xlsx";
        String objName = ContextHelper.getUserData().getWxUserId() + "/" + UUID.fastUUID().toString().replace("-", "");
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
            workbook.write(baos);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());) {
                s3FileUtil.upload("temp", objName, fileName, bais);
                return objName.replace("/", ",");
            } catch (Exception e) {
                logger.error("Update invoice bill failed: {}", e);
            }
        } catch (IOException e) {
            logger.error("Update invoice bill failed: {}", e);
        }
        return null;
    }

    private void processSheetBySupplierName(XSSFWorkbook workbook, String sheetName, List<InboundInvoiceDto> dataList, XSSFCellStyle titleStyle, XSSFCellStyle dataStyle, XSSFCellStyle resultStyle, XSSFCellStyle errResultStyle) throws NoSuchFieldException, IllegalAccessException {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < invoiceBillWorkbookHeaders.length; i++) {
            XSSFCell titleCell = titleRow.createCell(i);
            if (i == 0) {
                titleCell.setCellValue("进项发票订单明细表");
                titleCell.setCellStyle(titleStyle);
            }
        }
        CellRangeAddress titleRegion = new CellRangeAddress(0, 0, 0, invoiceBillWorkbookHeaders.length - 1);
        sheet.addMergedRegion(titleRegion);
        createRowHeader(sheet, dataStyle); // create header
        int rowNum = 2;
        Set<String> addedInvoiceApplyNums = Sets.newHashSet();
        Set<String> addedInvoiceNums = Sets.newHashSet();
        Map<String, List<InboundInvoiceDto>> dataMapByInvoiceApply = dataList.stream().collect(Collectors.groupingBy(InboundInvoiceDto::getBuyInvoiceApplyNumber));
        Set<Map.Entry<String, List<InboundInvoiceDto>>> entrySet = dataMapByInvoiceApply.entrySet();
        Iterator<Map.Entry<String, List<InboundInvoiceDto>>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<InboundInvoiceDto>> entry = iterator.next();
            String supplierName = entry.getKey();
            List<InboundInvoiceDto> dataListBySupplier = entry.getValue();
            Map<String, List<InboundInvoiceDto>> dataMapByInvoiceApplyNumber = dataListBySupplier.stream().collect(Collectors.groupingBy(InboundInvoiceDto::getBuyInvoiceApplyNumber));
            Set<Map.Entry<String, List<InboundInvoiceDto>>> entrySetByInvoiceApplyNumber = dataMapByInvoiceApplyNumber.entrySet();
            Iterator<Map.Entry<String, List<InboundInvoiceDto>>> iteratorByInvoiceApplyNumber = entrySetByInvoiceApplyNumber.iterator();
            while (iteratorByInvoiceApplyNumber.hasNext()) {
                Map.Entry<String, List<InboundInvoiceDto>> entryByInvoiceApplyNumber = iteratorByInvoiceApplyNumber.next();
                String invoiceApplyNumber = entryByInvoiceApplyNumber.getKey();
                List<InboundInvoiceDto> dataListByInvoiceApplyNumber = entryByInvoiceApplyNumber.getValue();
                Map<String, List<InboundInvoiceDto>> dataMapByInvoiceNo = dataListByInvoiceApplyNumber.stream().collect(Collectors.groupingBy(InboundInvoiceDto::getInvoiceNo));
                Set<Map.Entry<String, List<InboundInvoiceDto>>> entrySetByInvoiceNo = dataMapByInvoiceNo.entrySet();
                Iterator<Map.Entry<String, List<InboundInvoiceDto>>> iteratorByInvoiceNo = entrySetByInvoiceNo.iterator();
                while (iteratorByInvoiceNo.hasNext()) {
                    Map.Entry<String, List<InboundInvoiceDto>> entryByInvoiceNo = iteratorByInvoiceNo.next();
                    String invoiceNo = entryByInvoiceNo.getKey();
                    List<InboundInvoiceDto> dataListByInvoiceNo = entryByInvoiceNo.getValue();
                    for (int i = 0; i < dataListByInvoiceNo.size(); i ++) {
                        InboundInvoiceDto data = dataListByInvoiceNo.get(i);
                        XSSFRow row = sheet.createRow(rowNum ++);
                        for (int j = 0; j < invoiceBillWorkbookHeaders.length; j ++) {
                            ConditionPair conditionPair = invoiceBillWorkbookHeaders[j];
                            XSSFCell cell = row.createCell(j);
                            if (conditionPair.key.equals("ownerName")) {
                                String ownerName = ContextHelper.getUserData().getUsername();
                                cell.setCellValue(ownerName);
                                cell.setCellStyle(dataStyle);
                                continue;
                            }
                            Class<?> clazz = conditionPair.clazz;
                            Field field = InboundInvoiceDto.class.getDeclaredField(conditionPair.key);
                            boolean originalAccess = field.isAccessible();
                            field.setAccessible(true);
                            Object value = field.get(data);
                            field.setAccessible(originalAccess);
                            if (Objects.nonNull(value)) {
                                if (clazz == BigDecimal.class) {
                                    BigDecimal amount = new BigDecimal(value.toString());
//                                    if ("finalPrice".equals(conditionPair.key)) {
//                                        totalPrice = totalPrice.add(amount);
//                                    }
//                                    if ("buyInvoiceAmount".equals(conditionPair.key)) {
//                                        if (!addedInvoiceApplyNum.contains(data.getBuyInvoiceApplyNumber())) {
//                                            totalInvoiceAmount = totalInvoiceAmount.add(amount);
//                                            addedInvoiceApplyNum.add(data.getBuyInvoiceApplyNumber());
//                                        }
//                                    }
                                    cell.setCellValue(amount.doubleValue());
                                } else if (clazz == Boolean.class) {
                                    if ((Boolean) value) {
                                        cell.setCellValue("是");
                                    } else {
                                        cell.setCellValue("否");
                                    }
                                } else {
                                    cell.setCellValue(value.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void downloadInvoiceBill(String objName, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "进项发票订单明细表.xlsx";
        String fType = new MimetypesFileTypeMap().getContentType(fileName);
        response.reset();
        response.setHeader("content-type", fType + ";charset=utf-8");
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try (GetObjectResponse getObjectResponse = s3FileUtil.getFile("temp", objName.replace(",", "/"))) {
            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len=getObjectResponse.read(buf))!=-1){
                    os.write(buf,0,len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                response.setCharacterEncoding("utf-8");
                String UnicodeFileName = URLEncoder.encode(fileName, "UTF-8");
                response.addHeader("Content-Disposition", "attachment;fileName=" + UnicodeFileName);
                try (ServletOutputStream stream = response.getOutputStream()){
                    stream.write(bytes);
                    stream.flush();
                } finally {
                    s3FileUtil.remove("temp", objName);
                }
            }
        }
    }

}
