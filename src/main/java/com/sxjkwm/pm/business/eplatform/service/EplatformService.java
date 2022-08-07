package com.sxjkwm.pm.business.eplatform.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dao.EsDao;
import com.sxjkwm.pm.business.eplatform.dto.BillCheckingDto;
import com.sxjkwm.pm.business.eplatform.dto.InvoicePrintingDto;
import com.sxjkwm.pm.common.PageDataDto;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/7/25 8:19
 */
@Service
public class EplatformService {

    private static final Logger logger = LoggerFactory.getLogger(EplatformService.class);

    private static final String checkBillESIndexName = "epcd";

    private static final String printedInvoiceIndexName = "pepdi";


    private final EpDao epDao;
    private final EsDao esDao;
    private final S3FileUtil s3FileUtil;

    private static final ConditionPair[] invoiceBillWorkbookHeaders = {
            ConditionPair.of("supplierName", "供应商名称", String.class),
            ConditionPair.of("saleOrderNo", "平台订单号", Long.class),
            ConditionPair.of("finalPrice", "结算金额", BigDecimal.class),
            ConditionPair.of("saleInvoiceAmount", "发票金额", BigDecimal.class),
            ConditionPair.of("buyerOrgName", "采购单位", String.class),
            ConditionPair.of("saleInvoiceTitle", "发票抬头", String.class),
            ConditionPair.of("saleInvoiceRemark", "发票备注", String.class),
            ConditionPair.of("ownerName", "对接人", String.class),
    };
    private static final ConditionPair[] replacementPairs = {
            ConditionPair.of("supplierOrgId", "o.supplier_org_id", Long.class),
            ConditionPair.of("buyOrderNo", "o.buy_order_no", String.class),
            ConditionPair.of("buyBillNo", "bb.bill_code", String.class),
            ConditionPair.of("buyBillStatus", "bb.bb_status", Status.class),
            ConditionPair.of("buyInvoiceApplyNo", "bi.bi_invoice_apply_number", String.class),
            ConditionPair.of("buyInvoiceType", "bi.bi_invoice_type", Status.class),
            ConditionPair.of("buyInvoiceApplyStatus", "bi.bi_invoice_apply_status", Status.class),
            ConditionPair.of("buyInvoicePrintingStatus", "bi.bi_billing", Status.class),
            ConditionPair.of("buyInvoiceErrorMsg", "bi.bi_invoice_cancel_status", Status.class),
            ConditionPair.of("orderTimePeriod", "o.order_time", Date.class),
            ConditionPair.of("buyerOrgId", "o.buyer_org_id", Long.class),
            ConditionPair.of("saleOrderNo", "o.sale_order_no", String.class),
            ConditionPair.of("saleBillNo", "sb.bill_code", String.class),
            ConditionPair.of("saleBillStatus", "sb.sb_status", Status.class),
            ConditionPair.of("saleInvoiceApplyNo", "si.si_invoice_apply_number", String.class),
            ConditionPair.of("saleInvoiceType", "si.si_invoice_type", Status.class),
            ConditionPair.of("saleInvoiceApplyStatus", "si.si_invoice_apply_status", Status.class),
            ConditionPair.of("saleInvoicePrintingStatus", "si.si_billing", Status.class),
            ConditionPair.of("saleInvoiceErrorMsg", "si.si_invoice_cancel_status", Status.class)};


    @Autowired
    public EplatformService(EsDao esDao, EpDao epDao, S3FileUtil s3FileUtil) {
        this.esDao = esDao;
        this.epDao = epDao;
        this.s3FileUtil = s3FileUtil;
    }

    @Async
    public Boolean syncToES() {
        try {
            esDao.deleteIndex(checkBillESIndexName);
            List<BillCheckingDto> billCheckingDtos = queryData(Maps.newHashMap());
            if (CollectionUtils.isEmpty(billCheckingDtos)) {
                logger.warn("No EP data found when synchronizing data to ES");
                return Boolean.FALSE;
            }
            esDao.createIndex(checkBillESIndexName);
            for (BillCheckingDto dto : billCheckingDtos) {
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dto));
                esDao.addData(jsonObject, checkBillESIndexName, dto.getBuyOrderNo() + "_" + dto.getSaleOrderNo());
            }
            return Boolean.TRUE;
        } catch (IOException e) {
            logger.error("Synchronize EP data error: {}", e);
            return Boolean.FALSE;
        }
    }

    public List<Map<String, Object>> queryInEs(Map<String, String> conditionMap, Integer pageSize, Integer pageNo) throws IOException {
        BoolQueryBuilder queryCondition = QueryBuilders.boolQuery();
        if(!conditionMap.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = conditionMap.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                String field = entry.getKey();
                String strVal = entry.getValue();
                if (StringUtils.isNotBlank(strVal)) {
                    if ((!"orderStartTime".equals(field) && !"orderEndTime".equals(field))) {
                        queryCondition.must(QueryBuilders.matchQuery(field + ".keyword", strVal)); // .keyword is for searching exactly
                    }
                }
            }
            if (StringUtils.isNotBlank(conditionMap.get("orderStartTime")) &&
                    StringUtils.isNotBlank(conditionMap.get("orderEndTime"))) {
                Long startTime = Long.valueOf(conditionMap.get("orderStartTime"));
                Long endTime = Long.valueOf(conditionMap.get("orderEndTime"));
                queryCondition.must(QueryBuilders.rangeQuery("orderTime").from(startTime, true).to(endTime, true));
            } else if (StringUtils.isNotBlank(conditionMap.get("orderStartTime")) &&
                    StringUtils.isBlank(conditionMap.get("orderEndTime"))) {
                Long startTime = Long.valueOf(conditionMap.get("orderStartTime"));
                queryCondition.must(QueryBuilders.rangeQuery("orderTime").from(startTime, true));
            } else if (StringUtils.isBlank(conditionMap.get("orderStartTime")) &&
                    StringUtils.isNotBlank(conditionMap.get("orderEndTime"))) {
                Long endTime = Long.valueOf(conditionMap.get("orderEndTime"));
                queryCondition.must(QueryBuilders.rangeQuery("orderTime").to(endTime, true));
            }
        }
        List<Map<String, Object>> dataList = esDao.searchListData(checkBillESIndexName, queryCondition, pageSize, pageNo, null, null, null).getContent();
        return dataList;
    }

    public List<BillCheckingDto> queryData(Map<String, String> conditionMap) {
        List<Map<String, Object>> dataList = epDao.queryList(replaceConditions(conditionMap));
        List<BillCheckingDto> dtos = Lists.newArrayList();
        for (Map<String, Object> dataMap: dataList) {
            dtos.add(BillCheckingDto.fillData(dataMap));
        }
        return dtos;
    }

    private String replaceConditions(Map<String, String> conditionMap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = EpSql.billCheckingSql;
        String val = "";
        for (ConditionPair condition: replacementPairs) {
            String key = condition.getKey();
            String col = condition.getColName();
            String replaceKey = "{{" + key + "}}";
            Class<?> clazz = condition.getClazz();
            String mapVal = conditionMap.get(key);
            if (StringUtils.isBlank(mapVal) && !"orderTimePeriod".equalsIgnoreCase(key)) {
                val = "";
            } else {
                if (clazz == Date.class && "orderTimePeriod".equalsIgnoreCase(key)) {
                    if (StringUtils.isNotBlank(conditionMap.get("orderStartTime")) &&
                            StringUtils.isNotBlank(conditionMap.get("orderEndTime"))) {
                        Date startDate = Date.from(Instant.ofEpochMilli(Long.valueOf(conditionMap.get("orderStartTime"))));
                        String startDateStr = sdf.format(startDate);
                        Date endDate = Date.from(Instant.ofEpochMilli(Long.valueOf(conditionMap.get("orderEndTime"))));
                        String endDateStr = sdf.format(endDate);
                        val = " AND (" + col + " between '" + startDateStr + "' AND '" + endDateStr + "') ";
                    } else if (StringUtils.isNotBlank(conditionMap.get("orderStartTime")) &&
                            StringUtils.isBlank(conditionMap.get("orderEndTime"))) {
                        Date startDate = Date.from(Instant.ofEpochMilli(Long.valueOf(conditionMap.get("orderStartTime"))));
                        String startDateStr = sdf.format(startDate);
                        val = " AND " + col + " >= '" + startDateStr + "' ";
                    } else if (StringUtils.isBlank(conditionMap.get("orderStartTime")) &&
                            StringUtils.isNotBlank(conditionMap.get("orderEndTime"))) {
                        Date endDate = Date.from(Instant.ofEpochMilli(Long.valueOf(conditionMap.get("orderEndTime"))));
                        String endDateStr = sdf.format(endDate);
                        val= " AND " + col + " <= '" + endDateStr + "' ";
                    } else {
                        val = "";
                    }
                } else if (clazz == String.class) {
                    val = " AND " + col + " = '" + mapVal + "'";
                } else if (clazz == Status.class && mapVal.indexOf(",") > -1) {
                    val = " AND " + col + " IN (" + mapVal + ")";
                } else {
                    val = " AND " + col + " = " + mapVal;
                }
            }
            sql = sql.replace(replaceKey, val);
        }
        return sql;
    }

    public Boolean update(String id, Object item) throws IOException {
        return esDao.updateDataByIdNoRealTime(item, checkBillESIndexName, id);
    }

    public PageDataDto<Map<String, Object>> queryPrintedInvoiceInEs(Integer pageSize, Integer pageNo, Long startTime, Long endTime) throws IOException {
        BoolQueryBuilder queryCondition = QueryBuilders.boolQuery();
        if (Objects.nonNull(startTime) && Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("saleInvoiceFinishTime").from(startTime, true).to(endTime, true));
        } else if (Objects.nonNull(startTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("saleInvoiceFinishTime").from(startTime, true));
        } else if (Objects.nonNull(endTime)) {
            queryCondition.must(QueryBuilders.rangeQuery("saleInvoiceFinishTime").to(endTime, true));
        }
//        queryCondition.must(QueryBuilders.matchQuery("saleInvoiceTitle.keyword", "陕西高速机械化工程有限公司")); // .keyword is for searching exactly
        return esDao.searchListData(printedInvoiceIndexName, queryCondition, pageSize, pageNo, null, null, null);
    }

    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public void syncInvoicePrintedListInEs() throws IOException {
        logger.info("Start to synchronize invoice-bill data from DB to ES ...");
        esDao.deleteIndex(printedInvoiceIndexName);
        List<InvoicePrintingDto> dtos = queryInvoicePrintedList();
        if (CollectionUtils.isEmpty(dtos)) {
            return;
        }
        esDao.createIndex(printedInvoiceIndexName);
        for (InvoicePrintingDto dto: dtos) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dto));
            esDao.addData(jsonObject, printedInvoiceIndexName, dto.getSaleOrderNo().toString());
        }
        logger.info("Sync finished");
    }

    public List<InvoicePrintingDto> queryInvoicePrintedList() {
        List<Map<String, Object>> dataList = epDao.queryList(EpSql.invoicePrintingSql);
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        List<InvoicePrintingDto> dtos = Lists.newArrayList();
        for (Map<String, Object> dataRow: dataList) {
            dtos.add(InvoicePrintingDto.fillData(dataRow));
        }
        return dtos;
    }

    public String exportInvoiceBill(List<InvoicePrintingDto> dataList) throws NoSuchFieldException, IllegalAccessException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Map<String, List<InvoicePrintingDto>> dataMapByBuyerTitle = dataList.stream().collect(Collectors.groupingBy(InvoicePrintingDto::getSaleInvoiceTitle));
        Set<Map.Entry<String, List<InvoicePrintingDto>>> entrySet = dataMapByBuyerTitle.entrySet();
        Iterator<Map.Entry<String, List<InvoicePrintingDto>>> iterator = entrySet.iterator();
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

        while (iterator.hasNext()) {
            Map.Entry<String, List<InvoicePrintingDto>> entry = iterator.next();
            String sheetName = entry.getKey();
            processSheetByInvoiceTitle(workbook, sheetName, entry.getValue(), titleStyle, dataStyle, resultStyle);
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

    private void processSheetByInvoiceTitle(XSSFWorkbook workbook,
                                            String sheetName,
                                            List<InvoicePrintingDto> dataList,
                                            XSSFCellStyle titleStyle,
                                            XSSFCellStyle dataStyle,
                                            XSSFCellStyle resultStyle) throws NoSuchFieldException, IllegalAccessException {
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
        // fill data
        Map<String, List<InvoicePrintingDto>> dataMapBySupplier = dataList.stream().collect(Collectors.groupingBy(InvoicePrintingDto::getSupplierName));
        Set<Map.Entry<String, List<InvoicePrintingDto>>> entrySet = dataMapBySupplier.entrySet();
        Iterator<Map.Entry<String, List<InvoicePrintingDto>>> iterator = entrySet.iterator();
        BigDecimal totalPrice = new BigDecimal("0");
        BigDecimal totalInvoiceAmount = new BigDecimal("0");
        Set<String> addedInvoiceApplyNum = Sets.newHashSet();
        int rowNum = 2;
        while (iterator.hasNext()) {
            Map.Entry<String, List<InvoicePrintingDto>> entry = iterator.next();
            List<InvoicePrintingDto> bookDataList = entry.getValue();
            Map<String, List<InvoicePrintingDto>> dataMapByOrg = bookDataList.stream().collect(Collectors.groupingBy(InvoicePrintingDto::getBuyerOrgName));
            Set<Map.Entry<String, List<InvoicePrintingDto>>> orgEntrySet = dataMapByOrg.entrySet();
            Iterator<Map.Entry<String, List<InvoicePrintingDto>>> orgIterator = orgEntrySet.iterator();
            while (orgIterator.hasNext()) {
                Map.Entry<String, List<InvoicePrintingDto>> orgEntry = orgIterator.next();
                List<InvoicePrintingDto> orgDataList = orgEntry.getValue();
                int orgMergeFirstRow = 0;
                int orgRowSize = orgDataList.size();
                if (orgRowSize > 1) {
                    orgMergeFirstRow = rowNum;
                }
                Map<String, List<InvoicePrintingDto>> dataMapByInvoiceApply = orgDataList.stream().collect(Collectors.groupingBy(InvoicePrintingDto::getSaleInvoiceApplyNumber));
                Set<Map.Entry<String, List<InvoicePrintingDto>>> applyEntrySet = dataMapByInvoiceApply.entrySet();
                Iterator<Map.Entry<String, List<InvoicePrintingDto>>> applyIterator = applyEntrySet.iterator();
                while (applyIterator.hasNext()) {
                    Map.Entry<String, List<InvoicePrintingDto>> applyEntry = applyIterator.next();
                    List<InvoicePrintingDto> applyDataList = applyEntry.getValue();
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
                        InvoicePrintingDto data = applyDataList.get(k);
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
                            Field field = InvoicePrintingDto.class.getDeclaredField(conditionPair.key);
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
            if (i == invoiceBillWorkbookHeaders.length - 1) {
                if (!totalInvoiceAmount.equals(totalPrice)) {
                    totalPriceCell.getCellStyle().getFont().setColor(Font.COLOR_RED);
                    totalInvoiceAmountCell.getCellStyle().getFont().setColor(Font.COLOR_RED);
                }
            }
            cell.setCellStyle(resultStyle);
        }
        resultStyle.getFont().setColor(Font.COLOR_NORMAL);
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




    private static class ConditionPair {

        private String key;

        private String colName;

        private Class<?> clazz;

        public ConditionPair(String key, String colName, Class<?> clazz) {
            this.key = key;
            this.colName = colName;
            this.clazz = clazz;
        }

        public String getKey() {
            return key;
        }

        public String getColName() {
            return colName;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public static ConditionPair of(String key, String colName, Class<?> clazz) {
            return new ConditionPair(key, colName, clazz);
        }

    }

    /**
     * This is just an indication for condition value
     */
    private static class Status {
    }

}
