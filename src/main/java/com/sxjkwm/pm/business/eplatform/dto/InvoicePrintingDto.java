package com.sxjkwm.pm.business.eplatform.dto;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/8/7 12:06
 */
public class InvoicePrintingDto implements Serializable {

    private Long supplierOrgId; // 供应商ID

    private String supplierName; // 供应商名称

    private String buyerName; // 采购人

    private Long orderTime; // 下单时间

    private String buyerOrgName; // 采购单位名称

    private String buyerOrgFullName; //采购单位全称

    private Long saleOrderNo;  // 平台订单号

    private BigDecimal finalPrice; // 结算价格

    private String saleBillCode; // 账单编号

    private BigDecimal saleBillPrice; // 账单金额

    private String saleInvoiceApplyNumber; // 发票申请单号

    private String saleInvoiceCategory; // 发票类型

    private BigDecimal saleInvoiceAmount;  // 发票金额

    private String saleInvoiceTitle; // 发票抬头

    private String saleInvoiceRemark; // 发票备注

    private Long saleInvoiceFinishTime; // 发票打印时间

    public static InvoicePrintingDto fillData(Map<String, Object> dataRow) {
        InvoicePrintingDto dto = new InvoicePrintingDto();
        dto.setSupplierOrgId(MapUtils.getLong(dataRow, "supplier_org_id"));
        dto.setSupplierName(MapUtils.getString(dataRow, "supplier_name"));
        dto.setBuyerName(MapUtils.getString(dataRow, "buyer_name"));
        dto.setOrderTime(((Date) MapUtils.getObject(dataRow, "order_time")).getTime());
        dto.setBuyerOrgName(MapUtils.getString(dataRow, "buyer_org_name"));
        dto.setBuyerOrgFullName(MapUtils.getString(dataRow, "buyer_org_full_name"));
        dto.setSaleOrderNo(MapUtils.getLong(dataRow, "sale_order_no"));
        dto.setFinalPrice((BigDecimal) MapUtils.getNumber(dataRow, "final_price"));
        dto.setSaleBillCode(MapUtils.getString(dataRow, "bill_code"));
        dto.setSaleBillPrice((BigDecimal) MapUtils.getNumber(dataRow, "s_bill_price"));
        dto.setSaleInvoiceApplyNumber(MapUtils.getString(dataRow, "si_invoice_apply_number"));
        dto.setSaleInvoiceCategory(MapUtils.getString(dataRow, "sale_invoice_category"));
        dto.setSaleInvoiceAmount((BigDecimal) MapUtils.getNumber(dataRow, "si_amount"));
        dto.setSaleInvoiceTitle(MapUtils.getString(dataRow, "invoice_title"));
        dto.setSaleInvoiceRemark(MapUtils.getString(dataRow, "remark"));
        dto.setSaleInvoiceFinishTime(((Date) MapUtils.getObject(dataRow, "finish_time")).getTime());
        return dto;
    }

    public Long getSupplierOrgId() {
        return supplierOrgId;
    }

    public void setSupplierOrgId(Long supplierOrgId) {
        this.supplierOrgId = supplierOrgId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public String getBuyerOrgName() {
        return buyerOrgName;
    }

    public void setBuyerOrgName(String buyerOrgName) {
        this.buyerOrgName = buyerOrgName;
    }

    public String getBuyerOrgFullName() {
        return buyerOrgFullName;
    }

    public void setBuyerOrgFullName(String buyerOrgFullName) {
        this.buyerOrgFullName = buyerOrgFullName;
    }

    public Long getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(Long saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getSaleBillCode() {
        return saleBillCode;
    }

    public void setSaleBillCode(String saleBillCode) {
        this.saleBillCode = saleBillCode;
    }

    public BigDecimal getSaleBillPrice() {
        return saleBillPrice;
    }

    public void setSaleBillPrice(BigDecimal saleBillPrice) {
        this.saleBillPrice = saleBillPrice;
    }

    public String getSaleInvoiceApplyNumber() {
        return saleInvoiceApplyNumber;
    }

    public void setSaleInvoiceApplyNumber(String saleInvoiceApplyNumber) {
        this.saleInvoiceApplyNumber = saleInvoiceApplyNumber;
    }

    public String getSaleInvoiceCategory() {
        return saleInvoiceCategory;
    }

    public void setSaleInvoiceCategory(String saleInvoiceCategory) {
        this.saleInvoiceCategory = saleInvoiceCategory;
    }

    public BigDecimal getSaleInvoiceAmount() {
        return saleInvoiceAmount;
    }

    public void setSaleInvoiceAmount(BigDecimal saleInvoiceAmount) {
        this.saleInvoiceAmount = saleInvoiceAmount;
    }

    public String getSaleInvoiceTitle() {
        return saleInvoiceTitle;
    }

    public void setSaleInvoiceTitle(String saleInvoiceTitle) {
        this.saleInvoiceTitle = saleInvoiceTitle;
    }

    public String getSaleInvoiceRemark() {
        return saleInvoiceRemark;
    }

    public void setSaleInvoiceRemark(String saleInvoiceRemark) {
        this.saleInvoiceRemark = saleInvoiceRemark;
    }

    public Long getSaleInvoiceFinishTime() {
        return saleInvoiceFinishTime;
    }

    public void setSaleInvoiceFinishTime(Long saleInvoiceFinishTime) {
        this.saleInvoiceFinishTime = saleInvoiceFinishTime;
    }
}
