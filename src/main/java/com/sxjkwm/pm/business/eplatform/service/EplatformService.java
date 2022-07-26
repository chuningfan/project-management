package com.sxjkwm.pm.business.eplatform.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dto.BillCheckingDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/25 8:19
 */
@Service
public class EplatformService {

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

    private final EpDao epDao;

    @Autowired
    public EplatformService(EpDao epDao) {
        this.epDao = epDao;
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
