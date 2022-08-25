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
 * @date 2022/8/8 15:38
 */
@Service
public class InboundInvoiceService extends EpBaseService<InboundInvoiceDto> {

    private static final Logger logger = LoggerFactory.getLogger(InboundInvoiceService.class);

    static final String inboundInvoiceIndex = "pepii";

    private static final ConditionPair[] invoiceBillWorkbookHeaders = {
            ConditionPair.of("supplierName", "供应商名称", String.class),
            ConditionPair.of("buyOrderNo", "平台订单号", Long.class),
            ConditionPair.of("finalPrice", "结算金额", BigDecimal.class),
            ConditionPair.of("buyInvoiceAmount", "发票金额", BigDecimal.class),
            ConditionPair.of("buyerOrgName", "采购单位", String.class),
            ConditionPair.of("ownerName", "对接人", String.class),
            ConditionPair.of("hasOutbound", "是否已开销项", Boolean.class),
    };

    private final EpDao epDao;
    private final EsDao esDao;
    private final S3FileUtil s3FileUtil;

    @Autowired
    public InboundInvoiceService(EpDao epDao, EsDao esDao, S3FileUtil s3FileUtil) {
        super(epDao, esDao);
        this.epDao = epDao;
        this.esDao = esDao;
        this.s3FileUtil = s3FileUtil;
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

    public PageDataDto<Map<String, Object>> queryDataInEs(Integer pageSize, Integer pageNo, Long startTime, Long endTime, String[] supplierNames, String buyInvoiceApplyNumber) throws IOException {
        BoolQueryBuilder queryCondition = QueryBuilders.boolQuery();
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("operateTime").from(startTime, true).to(endTime, true));
        } else if (Objects.nonNull(startTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("operateTime").from(startTime, true));
        } else if (Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("operateTime").to(endTime, true));
        }
//        if (StringUtils.isNotBlank(supplierName)) {
//            queryCondition.must(QueryBuilders.matchQuery("supplierName.keyword", supplierName.trim()));
//        }
        if (StringUtils.isNotBlank(buyInvoiceApplyNumber)) {
            queryCondition.must(QueryBuilders.matchQuery("buyInvoiceApplyNumber.keyword", buyInvoiceApplyNumber.trim()));
        }
        if (Objects.nonNull(supplierNames) && supplierNames.length > 0) {
            queryCondition.must(QueryBuilders.termsQuery("supplierName.keyword", supplierNames));
        }
        return super.queryFromES(inboundInvoiceIndex, queryCondition, pageSize, pageNo);
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
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalInvoiceAmount = new BigDecimal("0");
        Set<String> addedInvoiceApplyNum = Sets.newHashSet();
        Map<String, List<InboundInvoiceDto>> dataMapByInvoiceApply = dataList.stream().collect(Collectors.groupingBy(InboundInvoiceDto::getBuyInvoiceApplyNumber));
        Set<Map.Entry<String, List<InboundInvoiceDto>>> entrySet = dataMapByInvoiceApply.entrySet();
        Iterator<Map.Entry<String, List<InboundInvoiceDto>>> iterator = entrySet.iterator();
        int mergeStartRowNum = 0;
        int mergeEndRowNum = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, List<InboundInvoiceDto>> entry = iterator.next();
            List<InboundInvoiceDto> invoiceApplyList = entry.getValue();
            int dataSize = invoiceApplyList.size();
            for (int i = 0; i < invoiceApplyList.size(); i ++) {
                if (dataSize > 1) {
                    if (i == 0) {
                        mergeStartRowNum = rowNum;
                    }
                    if (i == invoiceApplyList.size() - 1) {
                        mergeEndRowNum = mergeStartRowNum + dataSize - 1;
                    }
                }
                InboundInvoiceDto data = invoiceApplyList.get(i);
                XSSFRow row = sheet.createRow(rowNum);
                rowNum ++;
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
                            if ("finalPrice".equals(conditionPair.key)) {
                                totalPrice = totalPrice.add(amount);
                            }
                            if ("buyInvoiceAmount".equals(conditionPair.key)) {
                                if (!addedInvoiceApplyNum.contains(data.getBuyInvoiceApplyNumber())) {
                                    totalInvoiceAmount = totalInvoiceAmount.add(amount);
                                    addedInvoiceApplyNum.add(data.getBuyInvoiceApplyNumber());
                                }
                            }
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
                    cell.setCellStyle(dataStyle);
                    sheet.autoSizeColumn(j);
                }
            }
            if (dataSize > 1) {
                CellRangeAddress buyInvoicePriceRegion = new CellRangeAddress(mergeStartRowNum, mergeEndRowNum, 3, 3);
                sheet.addMergedRegion(buyInvoicePriceRegion);
            }
        }
        int totalDataSize = dataList.size();
        if (totalDataSize > 1) {
            CellRangeAddress supplierRegion = new CellRangeAddress(2, 2 + totalDataSize - 1, 0, 0);
            sheet.addMergedRegion(supplierRegion);
        }
        int totalRowNum = sheet.getPhysicalNumberOfRows();
        XSSFRow lastRow = sheet.createRow(totalRowNum);
        XSSFCell totalPriceCell = null;
        XSSFCell totalInvoiceAmountCell = null;
        for (int i = 0; i < invoiceBillWorkbookHeaders.length; i ++) {
            ConditionPair conditionPair = invoiceBillWorkbookHeaders[i];
            XSSFCell cell = lastRow.createCell(i);
            if (i == 0) {
                cell.setCellValue("合计：");
            } else {
                if ("finalPrice".equals(conditionPair.key)) {
                    totalPriceCell = cell;
                    cell.setCellValue(totalPrice.toString());
                }
                if ("buyInvoiceAmount".equals(conditionPair.key)) {
                    totalInvoiceAmountCell= cell;
                    cell.setCellValue(totalInvoiceAmount.toString());
                }
            }
            cell.setCellStyle(resultStyle);
        }
        if (!totalInvoiceAmount.equals(totalPrice)) {
            totalPriceCell.setCellStyle(errResultStyle);
            totalInvoiceAmountCell.setCellStyle(errResultStyle);
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

    private void createRowHeader(XSSFSheet sheet, XSSFCellStyle dataStyle) {
        XSSFRow row = sheet.createRow(1);
        for (int i = 0; i < invoiceBillWorkbookHeaders.length; i ++) {
            ConditionPair conditionPair = invoiceBillWorkbookHeaders[i];
            XSSFCell cell = row.createCell(i, CellType.STRING);
            cell.setCellValue(conditionPair.colName);
            cell.setCellStyle(dataStyle);
        }
    }

    @Override
    protected InboundInvoiceDto convert(Map<String, Object> dataRow) {
        return InboundInvoiceDto.fillData(dataRow);
    }

    @Override
    protected String getSQL() {
        return EpSql.inboundInvoiceSql;
    }
}
