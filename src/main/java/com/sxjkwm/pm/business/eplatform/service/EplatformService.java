package com.sxjkwm.pm.business.eplatform.service;

import com.sxjkwm.pm.business.eplatform.dto.BillCheckingDto;
import com.sxjkwm.pm.common.PageDataDto;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Vic.Chu
 * @date 2022/7/25 8:19
 */
//@Service
//public class EplatformService extends EpBaseService<BillCheckingDto> {
//
//    private static final Logger logger = LoggerFactory.getLogger(EplatformService.class);
//
//    private static final String checkBillESIndexName = "epcd";
//
//
//    private static final ConditionPair[] replacementPairs = {
//            ConditionPair.of("supplierOrgId", "o.supplier_org_id", Long.class),
//            ConditionPair.of("buyOrderNo", "o.buy_order_no", String.class),
//            ConditionPair.of("buyBillNo", "bb.bill_code", String.class),
//            ConditionPair.of("buyBillStatus", "bb.bb_status", Status.class),
//            ConditionPair.of("buyInvoiceApplyNo", "bi.bi_invoice_apply_number", String.class),
//            ConditionPair.of("buyInvoiceType", "bi.bi_invoice_type", Status.class),
//            ConditionPair.of("buyInvoiceApplyStatus", "bi.bi_invoice_apply_status", Status.class),
//            ConditionPair.of("buyInvoicePrintingStatus", "bi.bi_billing", Status.class),
//            ConditionPair.of("buyInvoiceErrorMsg", "bi.bi_invoice_cancel_status", Status.class),
//            ConditionPair.of("orderTimePeriod", "o.order_time", Date.class),
//            ConditionPair.of("buyerOrgId", "o.buyer_org_id", Long.class),
//            ConditionPair.of("saleOrderNo", "o.sale_order_no", String.class),
//            ConditionPair.of("saleBillNo", "sb.bill_code", String.class),
//            ConditionPair.of("saleBillStatus", "sb.sb_status", Status.class),
//            ConditionPair.of("saleInvoiceApplyNo", "si.si_invoice_apply_number", String.class),
//            ConditionPair.of("saleInvoiceType", "si.si_invoice_type", Status.class),
//            ConditionPair.of("saleInvoiceApplyStatus", "si.si_invoice_apply_status", Status.class),
//            ConditionPair.of("saleInvoicePrintingStatus", "si.si_billing", Status.class),
//            ConditionPair.of("saleInvoiceErrorMsg", "si.si_invoice_cancel_status", Status.class)};
//
//
//    @Async
//    public Boolean syncToES() {
//        try {
//            super.syncToEs(checkBillESIndexName, null);
//            return Boolean.TRUE;
//        } catch (Exception e) {
//            logger.error("Sync order data failed: {}", e);
//            return Boolean.FALSE;
//        }
//    }
//
//    public PageDataDto<Map<String, Object>> queryInEs(Map<String, String> conditionMap, Integer pageSize, Integer pageNo) throws IOException {
//        BoolQueryBuilder queryCondition = QueryBuilders.boolQuery();
//        if(!conditionMap.isEmpty()) {
//            Set<Map.Entry<String, String>> entrySet = conditionMap.entrySet();
//            Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, String> entry = iterator.next();
//                String field = entry.getKey();
//                String strVal = entry.getValue();
//                if (StringUtils.isNotBlank(strVal)) {
//                    if ((!"orderStartTime".equals(field) && !"orderEndTime".equals(field))) {
//                        queryCondition.must(QueryBuilders.matchQuery(field + ".keyword", strVal)); // .keyword is for searching exactly
//                    }
//                }
//            }
//            if (StringUtils.isNotBlank(conditionMap.get("orderStartTime")) &&
//                    StringUtils.isNotBlank(conditionMap.get("orderEndTime"))) {
//                Long startTime = Long.valueOf(conditionMap.get("orderStartTime"));
//                Long endTime = Long.valueOf(conditionMap.get("orderEndTime"));
//                queryCondition.must(QueryBuilders.rangeQuery("orderTime").from(startTime, true).to(endTime, true));
//            } else if (StringUtils.isNotBlank(conditionMap.get("orderStartTime")) &&
//                    StringUtils.isBlank(conditionMap.get("orderEndTime"))) {
//                Long startTime = Long.valueOf(conditionMap.get("orderStartTime"));
//                queryCondition.must(QueryBuilders.rangeQuery("orderTime").from(startTime, true));
//            } else if (StringUtils.isBlank(conditionMap.get("orderStartTime")) &&
//                    StringUtils.isNotBlank(conditionMap.get("orderEndTime"))) {
//                Long endTime = Long.valueOf(conditionMap.get("orderEndTime"));
//                queryCondition.must(QueryBuilders.rangeQuery("orderTime").to(endTime, true));
//            }
//        }
//        return super.queryFromES(checkBillESIndexName, queryCondition, pageSize, pageNo);
//    }
//
//    @Override
//    protected BillCheckingDto convert(Map<String, Object> dataRow) {
//        return BillCheckingDto.fillData(dataRow);
//    }
//
//    @Override
//    protected String getSQL() {
//        return EpSql.billCheckingSql;
//    }
//}
