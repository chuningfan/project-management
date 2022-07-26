package com.sxjkwm.pm.business.eplatform.service;

/**
 * @author Vic.Chu
 * @date 2022/7/25 9:24
 */
public class EpSql {

    static String billCheckingSql = "SELECT o.org_name as supplierName, o.buy_order_no as buyOrderNo, bb.bill_code as buyBillNo, " +
            "bb.b_bill_price buyBillAmount, (CASE WHEN bb.bb_status = 0 THEN '待确认' WHEN bb.bb_status = 1 THEN '已驳回' WHEN bb.bb_status = 2 THEN '已确认' WHEN bb.bb_status = 3 THEN '已完成' WHEN bb.bb_status = 4 THEN '已作废' ELSE '未开始' END) as buyBillStatus, " +
            "bi.bi_invoice_apply_number buyInvoiceApplyNo, bi.bi_amount as buyInvoiceAmount, (CASE WHEN bi.bi_invoice_type = 1 THEN '普通纸质票' WHEN bi.bi_invoice_type = 2 THEN '专票' WHEN bi.bi_invoice_type = 3 THEN '普通电子票' END) as buyInvoiceType, " +
            "(CASE WHEN bi.bi_invoice_apply_status = 0 THEN '处理中' WHEN bi.bi_invoice_apply_status = 1 THEN '已完成' WHEN bi.bi_invoice_apply_status = 2 THEN '已驳回' WHEN bi.bi_invoice_apply_status = 3 THEN '已作废' WHEN bi.bi_invoice_apply_status = 4 THEN '已退回' WHEN bi.bi_invoice_apply_status = 5 THEN '待确认' ELSE '未开始' END) as buyInvoiceApplyStatus, " +
            "(CASE WHEN bi.bi_billing = 0 THEN '开票中' WHEN bi.bi_billing = 1 THEN '开票完成' WHEN bi.bi_billing = 2 THEN '开票失败' ELSE '未开始' END) as buyInvoicePrintingStatus, " +
            "(CASE WHEN bi.bi_invoice_cancel_status = 0 THEN '待审核' WHEN bi.bi_invoice_cancel_status = 1 THEN '已作废' WHEN bi.bi_invoice_cancel_status = 2 THEN '冲红中' WHEN bi.bi_invoice_cancel_status = 3 THEN '已冲红' WHEN bi.bi_invoice_cancel_status = 4 THEN '已驳回' WHEN bi.bi_invoice_cancel_status = 5 THEN '失败' ELSE '无' END) as buyInvoiceErrorMsg, " +
            "o.buyer_name as buyerName, o.order_time as orderTime, o.buyer_org_name as buyerOrgName, o.sale_order_no as saleOrderNo, o.final_price as saleFinalPrice, sb.bill_code as saleBillNo, sb.s_bill_price as saleBillAmount, " +
            "(CASE WHEN sb.sb_status = 0 THEN '待确认' WHEN sb.sb_status = 1 THEN '已驳回' WHEN sb.sb_status = 2 THEN '已确认' WHEN sb.sb_status = 3 THEN '已完成' WHEN sb.sb_status = 4 THEN '已作废' ELSE '未开始' END) as saleBillStatus, " +
            "si.si_invoice_apply_number as saleInvoiceApplyNo, (CASE WHEN si.si_invoice_type = 1 THEN '普通纸质票' WHEN si.si_invoice_type = 2 THEN '专票' WHEN si.si_invoice_type = 3 THEN '普通电子票' END) as saleInvoiceType, " +
            "si.si_amount as saleInvoiceAmount, (CASE WHEN si.si_invoice_apply_status = 0 THEN '处理中' WHEN si.si_invoice_apply_status = 1 THEN '已完成' WHEN si.si_invoice_apply_status = 2 THEN '已驳回' WHEN si.si_invoice_apply_status = 3 THEN '已作废' WHEN si.si_invoice_apply_status = 4 THEN '已退回' WHEN si.si_invoice_apply_status = 5 THEN '待确认' ELSE '未开始' END) as saleInvoiceApplyStatus, " +
            "(CASE WHEN si.si_billing = 0 THEN '开票中' WHEN si.si_billing = 1 THEN '开票完成' WHEN si.si_billing = 2 THEN '开票失败' ELSE '未开始' END) as saleInvoicePrintingStatus, " +
            "(CASE WHEN si.si_invoice_cancel_status = 0 THEN '待审核' WHEN si.si_invoice_cancel_status = 1 THEN '已作废' WHEN si.si_invoice_cancel_status = 2 THEN '冲红中' WHEN si.si_invoice_cancel_status = 3 THEN '已冲红' WHEN si.si_invoice_cancel_status = 4 THEN '已驳回' WHEN si.si_invoice_cancel_status = 5 THEN '失败' ELSE '无' END) as saleInvoiceErrorMsg " +
            "FROM (" +
            " SELECT org.id AS supplier_org_id, oi.order_no AS buy_order_no, oi.order_no + 1 AS sale_order_no, oi.final_price, org.name AS org_name, oi.order_time, ua.user_name AS buyer_name, ua.legal_entity_organization_id AS buyer_org_id, ua.legal_entity_organization_name AS buyer_org_name " +
            " FROM service_order.order_info oi " +
            " LEFT JOIN service_order.`reseller_order_info` roi ON oi.`order_no` = roi.`order_no` AND roi.`yn`=1 " +
            " LEFT JOIN service_user.`shop_info` si ON si.shop_id = oi.shop_id AND si.yn = 1 " +
            " LEFT JOIN service_user.organization org ON org.id = si.organize_id AND org.yn = 1 " +
            " LEFT JOIN service_user.`user_account` ua ON ua.id = oi.buyer_id AND ua.yn = 1 " +
            " WHERE oi.order_status = 40 AND roi.id IS NOT NULL AND oi.`yn`=1 " +
            ") o " +
            " LEFT JOIN (" +
            " SELECT otr.order_no, bi.`bill_code`, bi.`status` AS bb_status, bi.`bill_price` AS b_bill_price  " +
            " FROM service_order.`bill_order` bo " +
            " INNER JOIN service_order.`order_third_relation` otr ON otr.third_order_no = bo.order_id AND otr.`yn` = 1 " +
            " INNER JOIN service_order.`bill_infos` bi ON bi.id = bo.`bill_id` AND bi.`yn` = 1 AND bi.has_reseller_create IS NULL " +
            ") bb ON bb.order_no = o.buy_order_no " +
            " LEFT JOIN (" +
            " SELECT t.order_id, t.bill_code, bbi.status AS sb_status, bbi.`bill_price` AS s_bill_price FROM (" +
            " SELECT bo.order_id, MAX(bi.`bill_code`) AS bill_code FROM service_order.`bill_order` bo INNER JOIN service_order.`bill_infos` bi ON bi.id = bo.`bill_id` AND bi.has_reseller_create = 1 AND bi.`yn` = 1 GROUP BY bo.order_id " +
            " ) t " +
            " INNER JOIN service_order.`bill_infos` bbi ON bbi.bill_code = t.bill_code " +
            ") sb ON sb.order_id = o.sale_order_no " +
            " LEFT JOIN (" +
            " SELECT otr.`order_no` AS bi_order_no, ia.`apply_number` AS bi_invoice_apply_number, ia.`apply_status` AS bi_invoice_apply_status, ia.`tatal_amount` AS bi_amount, ia.`billing` AS bi_billing, ia.`cancel_status` AS bi_invoice_cancel_status, ia.invoice_type AS bi_invoice_type " +
            " FROM service_order.`invoice_apply_order_relation` iaor " +
            " INNER JOIN service_order.`invoice_apply` ia ON ia.id = iaor.`invoice_apply_id` AND ia.`sql_type` = 0 AND ia.`yn` = 1 " +
            " INNER JOIN service_order.`order_third_relation` otr ON otr.`third_order_no` = iaor.`order_id` AND otr.`yn` = 1 " +
            ") bi ON bi.bi_order_no = o.buy_order_no " +
            " LEFT JOIN (" +
            " SELECT t.si_order_no, ia.`apply_number` AS si_invoice_apply_number, ia.`apply_status` AS si_invoice_apply_status, ia.`tatal_amount` AS si_amount, ia.`billing` AS si_billing, ia.`cancel_status` AS si_invoice_cancel_status, ia.invoice_type AS si_invoice_type " +
            " FROM service_order.`invoice_apply` ia " +
            " INNER JOIN ( " +
            " SELECT iaor.`order_no` AS si_order_no, MAX(ia.`apply_number`) AS si_invoice_apply_number " +
            " FROM service_order.`invoice_apply_order_relation` iaor " +
            " INNER JOIN service_order.`invoice_apply` ia ON ia.id = iaor.`invoice_apply_id` AND ia.`sql_type` = 1 AND ia.`yn` = 1 " +
            " GROUP BY iaor.`order_no`  " +
            " ) t ON t.si_invoice_apply_number = ia.apply_number" +
            ") si ON si.si_order_no = o.sale_order_no " +
            " WHERE 1 = 1 {{supplierOrgId}} {{buyOrderNo}} {{buyBillNo}} {{buyBillStatus}} {{buyInvoiceApplyNo}} {{buyInvoiceType}} {{buyInvoiceApplyStatus}} " +
            " {{buyInvoicePrintingStatus}} {{buyInvoiceErrorMsg}} {{orderTimePeriod}} {{buyerOrgId}} {{saleOrderNo}} {{saleBillNo}} {{saleBillStatus}} " +
            " {{saleInvoiceApplyNo}} {{saleInvoiceType}} {{saleInvoiceApplyStatus}} {{saleInvoicePrintingStatus}} {{saleInvoiceErrorMsg}} " +
            " ORDER BY bb.bill_code LIMIT 20000 ";

}
