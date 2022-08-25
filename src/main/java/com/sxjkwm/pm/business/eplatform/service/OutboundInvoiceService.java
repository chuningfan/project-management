package com.sxjkwm.pm.business.eplatform.service;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dao.EsDao;
import com.sxjkwm.pm.business.eplatform.dto.InboundInvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.OutboundInvoiceDto;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/8/8 15:30
 */
@Service
public class OutboundInvoiceService extends EpBaseService<OutboundInvoiceDto> {

    private static final Logger logger = LoggerFactory.getLogger(OutboundInvoiceService.class);

    static final String outboundInvoiceIndex = "pepdi";

    private final S3FileUtil s3FileUtil;

    private final EpDao epDao;

    private final EsDao esDao;

    @Autowired
    public OutboundInvoiceService(S3FileUtil s3FileUtil, EpDao epDao, EsDao esDao) {
        super(epDao, esDao);
        this.s3FileUtil = s3FileUtil;
        this.epDao = epDao;
        this.esDao = esDao;
    }

    private static final ConditionPair[] invoiceBillWorkbookHeaders = {
            ConditionPair.of("supplierName", "供应商名称", String.class),
            ConditionPair.of("saleOrderNo", "平台订单号", Long.class),
            ConditionPair.of("finalPrice", "结算金额", BigDecimal.class),
            ConditionPair.of("saleInvoiceAmount", "发票金额", BigDecimal.class),
            ConditionPair.of("buyerOrgName", "采购单位", String.class),
            ConditionPair.of("saleInvoiceTitle", "发票抬头", String.class),
            ConditionPair.of("saleInvoiceRemark", "发票备注", String.class),
            ConditionPair.of("ownerName", "对接人", String.class),
            ConditionPair.of("thirdPartyOrderNo", "三方订单号", String.class),
            ConditionPair.of("hasInbound", "进项是否已开", Boolean.class),
            ConditionPair.of("saleInvoiceFinishTime", "销项发票开票时间", Date.class),
    };

    public PageDataDto<Map<String, Object>> queryPrintedInvoiceInEs(Integer pageSize, Integer pageNo, Long startTime, Long endTime, String[] buyerOrgs, String invoiceTitle, String invoiceApplyNum, String[] supplierNames) throws IOException {
        BoolQueryBuilder queryCondition = QueryBuilders.boolQuery();
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("saleInvoiceFinishTime").from(startTime, true).to(endTime, true));
        } else if (Objects.nonNull(startTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("saleInvoiceFinishTime").from(startTime, true));
        } else if (Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("saleInvoiceFinishTime").to(endTime, true));
        }
        if (Objects.nonNull(buyerOrgs) && buyerOrgs.length > 0) {
            for (int i = 0; i < buyerOrgs.length; i ++) {
                String org = buyerOrgs[i].trim();
                buyerOrgs[i] = org;
            }
            queryCondition.must(QueryBuilders.termsQuery("buyerOrgName.keyword", buyerOrgs));
        }
        if (StringUtils.isNotBlank(invoiceTitle)) {
            queryCondition.must(QueryBuilders.matchQuery("saleInvoiceTitle.keyword", invoiceTitle.trim())); // .keyword is for searching exactly
        }
        if (StringUtils.isNotBlank(invoiceApplyNum)) {
            queryCondition.must(QueryBuilders.matchQuery("saleInvoiceApplyNumber.keyword", invoiceApplyNum)); // .keyword is for searching exactly
        }
        if (Objects.nonNull(supplierNames) && supplierNames.length > 0) {
            queryCondition.must(QueryBuilders.termsQuery("supplierName.keyword", supplierNames));
        }
        return super.queryFromES(outboundInvoiceIndex, queryCondition, pageSize, pageNo);
    }

    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public Boolean syncInvoicePrintedListInEs() {
        try {
            super.syncToEs(outboundInvoiceIndex, "saleOrderNo");
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.error("Sync invoice bill failed: {}", e);
            return Boolean.FALSE;
        }
    }

    public String exportInvoiceBill(List<OutboundInvoiceDto> dataList, boolean mergeData) throws NoSuchFieldException, IllegalAccessException {
        // process property[hasInbound]
        for (OutboundInvoiceDto outbound: dataList) {
            Long inboundId = outbound.getSaleOrderNo() - 1;
            Boolean hasInbound = false;
            try {
                Map<String,Object> resp = esDao.searchDataById(InboundInvoiceService.inboundInvoiceIndex, inboundId.toString(), "buyInvoiceApplyNumber");
                if (Objects.nonNull(resp)) {
                    Object resObj = resp.get("buyInvoiceApplyNumber");
                    if (Objects.nonNull(resObj)) {
                        String num = resObj.toString();
                        if (StringUtils.isNotBlank(num)) {
                            hasInbound = true;
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Search outbound data failed: {}", e);
            }
            outbound.setHasInbound(hasInbound);
        }
        XSSFWorkbook workbook = new XSSFWorkbook();

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (mergeData) {
            processAllData(workbook, "merged_data", dataList, titleStyle, dataStyle, simpleDateFormat);
        } else {
            Map<String, List<OutboundInvoiceDto>> dataMapByBuyerTitle = dataList.stream().collect(Collectors.groupingBy(OutboundInvoiceDto::getSaleInvoiceTitle));
            Set<Map.Entry<String, List<OutboundInvoiceDto>>> entrySet = dataMapByBuyerTitle.entrySet();
            Iterator<Map.Entry<String, List<OutboundInvoiceDto>>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<OutboundInvoiceDto>> entry = iterator.next();
                String sheetName = entry.getKey();
                processSheetByInvoiceTitle(workbook, sheetName, entry.getValue(), titleStyle, dataStyle, resultStyle, errResultStyle, simpleDateFormat);
            }
        }
        String fileName = "销项发票订单明细表.xlsx";
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

    public void downloadInvoiceBill(String objName, HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = "销项发票订单明细表.xlsx";
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

    private void processAllData(XSSFWorkbook workbook,
                                String sheetName,
                                List<OutboundInvoiceDto> dataList,
                                XSSFCellStyle titleStyle,
                                XSSFCellStyle dataStyle,
                                SimpleDateFormat simpleDateFormat) throws NoSuchFieldException, IllegalAccessException {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < invoiceBillWorkbookHeaders.length; i++) {
            XSSFCell titleCell = titleRow.createCell(i);
            if (i == 0) {
                titleCell.setCellValue("销项发票订单明细表");
                titleCell.setCellStyle(titleStyle);
            }
        }
        CellRangeAddress titleRegion = new CellRangeAddress(0, 0, 0, invoiceBillWorkbookHeaders.length - 1);
        sheet.addMergedRegion(titleRegion);
        createRowHeader(sheet, dataStyle); // create header
        int rowNum = 2;
        List<OutboundInvoiceDto> bookDataList = dataList;
        for (OutboundInvoiceDto outbound: bookDataList) {
            XSSFRow row = sheet.createRow(rowNum ++);
            for (int i = 0; i < invoiceBillWorkbookHeaders.length; i++) {
                ConditionPair conditionPair = invoiceBillWorkbookHeaders[i];
                XSSFCell cell = row.createCell(i);
                if (conditionPair.key.equals("ownerName")) {
                    String ownerName = ContextHelper.getUserData().getUsername();
                    cell.setCellValue(ownerName);
                    cell.setCellStyle(dataStyle);
                    continue;
                }
                Class<?> clazz = conditionPair.clazz;
                Field field = OutboundInvoiceDto.class.getDeclaredField(conditionPair.key);
                boolean originalAccess = field.isAccessible();
                field.setAccessible(true);
                Object value = field.get(outbound);
                field.setAccessible(originalAccess);
                if (Objects.nonNull(value)) {
                    if (clazz == BigDecimal.class) {
                        BigDecimal amount = new BigDecimal(value.toString());
                        cell.setCellValue(amount.doubleValue());
                    } else if (clazz == Boolean.class) {
                        if ((Boolean) value) {
                            cell.setCellValue("是");
                        } else {
                            cell.setCellValue("否");
                        }
                    } else if (clazz == Date.class) {
                        Long date = (Long) value;
                        if (Objects.nonNull(date)) {
                            cell.setCellValue(simpleDateFormat.format(Date.from(Instant.ofEpochMilli(date))));
                        }
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
                cell.setCellStyle(dataStyle);
                sheet.autoSizeColumn(i);
            }
        }
    }

    private void processSheetByInvoiceTitle(XSSFWorkbook workbook,
                                            String sheetName,
                                            List<OutboundInvoiceDto> dataList,
                                            XSSFCellStyle titleStyle,
                                            XSSFCellStyle dataStyle,
                                            XSSFCellStyle resultStyle,
                                            XSSFCellStyle errResultStyle,
                                            SimpleDateFormat simpleDateFormat) throws NoSuchFieldException, IllegalAccessException {
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow titleRow = sheet.createRow(0);
        for (int i = 0; i < invoiceBillWorkbookHeaders.length; i++) {
            XSSFCell titleCell = titleRow.createCell(i);
            if (i == 0) {
                titleCell.setCellValue("销项发票订单明细表");
                titleCell.setCellStyle(titleStyle);
            }
        }
        CellRangeAddress titleRegion = new CellRangeAddress(0, 0, 0, invoiceBillWorkbookHeaders.length - 1);
        sheet.addMergedRegion(titleRegion);
        createRowHeader(sheet, dataStyle); // create header
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalInvoiceAmount = new BigDecimal("0");
        Set<String> addedInvoiceApplyNum = Sets.newHashSet();
        int rowNum = 2;
        List<OutboundInvoiceDto> bookDataList = dataList;
        Map<String, List<OutboundInvoiceDto>> dataMapByOrg = bookDataList.stream().collect(Collectors.groupingBy(OutboundInvoiceDto::getBuyerOrgName));
        Set<Map.Entry<String, List<OutboundInvoiceDto>>> orgEntrySet = dataMapByOrg.entrySet();
        Iterator<Map.Entry<String, List<OutboundInvoiceDto>>> orgIterator = orgEntrySet.iterator();
        while (orgIterator.hasNext()) {
            Map.Entry<String, List<OutboundInvoiceDto>> orgEntry = orgIterator.next();
            List<OutboundInvoiceDto> orgDataList = orgEntry.getValue();
            int orgMergeFirstRow = 0;
            int orgRowSize = orgDataList.size();
            if (orgRowSize > 1) {
                orgMergeFirstRow = rowNum;
            }
            Map<String, List<OutboundInvoiceDto>> dataMapByInvoiceApply = orgDataList.stream().collect(Collectors.groupingBy(OutboundInvoiceDto::getSaleInvoiceApplyNumber));
            Set<Map.Entry<String, List<OutboundInvoiceDto>>> applyEntrySet = dataMapByInvoiceApply.entrySet();
            Iterator<Map.Entry<String, List<OutboundInvoiceDto>>> applyIterator = applyEntrySet.iterator();
            while (applyIterator.hasNext()) {
                Map.Entry<String, List<OutboundInvoiceDto>> applyEntry = applyIterator.next();
                List<OutboundInvoiceDto> applyDataList = applyEntry.getValue();
                int rowSize = applyDataList.size();
                int mergeFirstRow = 0;
                int mergeLastRow = 0;
                for (int k = 0; k < applyDataList.size(); k ++) {
                    if (rowSize > 1) {
                        if (k == 0) { // 需合并的第一行
                            mergeFirstRow = rowNum;
                        }
                        if (k == rowSize - 1) { //需合并的最后一行
                            mergeLastRow = mergeFirstRow + rowSize - 1;
                        }
                    }
                    OutboundInvoiceDto data = applyDataList.get(k);
                    XSSFRow row = sheet.createRow(rowNum);
                    rowNum ++;
                    for (int i = 0; i < invoiceBillWorkbookHeaders.length; i++) {
                        ConditionPair conditionPair = invoiceBillWorkbookHeaders[i];
                        XSSFCell cell = row.createCell(i);
                        if (conditionPair.key.equals("ownerName")) {
                            String ownerName = ContextHelper.getUserData().getUsername();
                            cell.setCellValue(ownerName);
                            cell.setCellStyle(dataStyle);
                            continue;
                        }
                        Class<?> clazz = conditionPair.clazz;
                        Field field = OutboundInvoiceDto.class.getDeclaredField(conditionPair.key);
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
                                if ("saleInvoiceAmount".equals(conditionPair.key)) {
                                    if (!addedInvoiceApplyNum.contains(data.getSaleInvoiceApplyNumber())) {
                                        totalInvoiceAmount = totalInvoiceAmount.add(amount);
                                        addedInvoiceApplyNum.add(data.getSaleInvoiceApplyNumber());
                                    }
                                }
                                cell.setCellValue(amount.doubleValue());
                            } else if (clazz == Boolean.class) {
                                if ((Boolean) value) {
                                    cell.setCellValue("是");
                                } else {
                                    cell.setCellValue("否");
                                }
                            } else if (clazz == Date.class) {
                                Long date = (Long) value;
                                if (Objects.nonNull(date)) {
                                    cell.setCellValue(simpleDateFormat.format(Date.from(Instant.ofEpochMilli(date))));
                                }
                            } else {
                                cell.setCellValue(value.toString());
                            }
                        }
                        cell.setCellStyle(dataStyle);
                        sheet.autoSizeColumn(i);
                    }
                }
                if (rowSize > 1) {
                    CellRangeAddress saleInvoicePriceRegion = new CellRangeAddress(mergeFirstRow, mergeLastRow, 3, 3);
                    sheet.addMergedRegion(saleInvoicePriceRegion);
                }
            }
            if (orgRowSize > 1) {
                CellRangeAddress orgRegion = new CellRangeAddress(orgMergeFirstRow, orgMergeFirstRow + orgRowSize - 1, 4, 4);
                sheet.addMergedRegion(orgRegion);
            }
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
                if ("saleInvoiceAmount".equals(conditionPair.key)) {
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
    protected OutboundInvoiceDto convert(Map dataRow) {
        return OutboundInvoiceDto.fillData(dataRow);
    }

    @Override
    protected String getSQL() {
        return EpSql.outboundInvoiceSql;
    }
}
