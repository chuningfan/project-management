package com.sxjkwm.pm.business.eplatform.dto;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/8/8 16:58
 */
public class InboundInvoiceDto implements Serializable {

    private Long supplierOrgId; // 供应商ID

    private String supplierName; // 供应商名称

    private String buyerOrgName; // 采购单位名称

    private String buyerOrgFullName; //采购单位全称

    private Long buyOrderNo;  // 平台订单号

    private BigDecimal finalPrice; // 结算价格

    private String buyBillCode; // 账单编号

    private BigDecimal buyBillPrice; // 账单金额

    private String buyInvoiceApplyNumber; // 发票申请单号

    private BigDecimal buyInvoiceAmount;  // 发票金额

    private Integer invoicecCount; // 发票数量

    private Long operateTime; // 发票打印时间

    public static InboundInvoiceDto fillData(Map<String, Object> dataRow) {
        InboundInvoiceDto dto = new InboundInvoiceDto();
        dto.setSupplierOrgId(MapUtils.getLong(dataRow, "supplier_org_id"));
        dto.setSupplierName(MapUtils.getString(dataRow, "supplier_name").trim());
        dto.setBuyerOrgName(MapUtils.getString(dataRow, "buyer_org_name").trim());
        dto.setBuyerOrgFullName(MapUtils.getString(dataRow, "buyer_org_full_name"));
        dto.setBuyOrderNo(MapUtils.getLong(dataRow, "buy_order_no"));
        dto.setFinalPrice((BigDecimal) MapUtils.getNumber(dataRow, "final_price"));
        dto.setBuyBillCode(MapUtils.getString(dataRow, "bill_code"));
        dto.setBuyBillPrice((BigDecimal) MapUtils.getNumber(dataRow, "bill_price"));
        dto.setBuyInvoiceApplyNumber(MapUtils.getString(dataRow, "apply_number"));
        dto.setBuyInvoiceAmount((BigDecimal) MapUtils.getNumber(dataRow, "tatal_amount"));
        dto.setInvoicecCount(MapUtils.getInteger(dataRow, "invoice_num"));
        dto.setOperateTime(((Date) MapUtils.getObject(dataRow, "modified")).getTime());
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

    public Long getBuyOrderNo() {
        return buyOrderNo;
    }

    public void setBuyOrderNo(Long buyOrderNo) {
        this.buyOrderNo = buyOrderNo;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getBuyBillCode() {
        return buyBillCode;
    }

    public void setBuyBillCode(String buyBillCode) {
        this.buyBillCode = buyBillCode;
    }

    public BigDecimal getBuyBillPrice() {
        return buyBillPrice;
    }

    public void setBuyBillPrice(BigDecimal buyBillPrice) {
        this.buyBillPrice = buyBillPrice;
    }

    public String getBuyInvoiceApplyNumber() {
        return buyInvoiceApplyNumber;
    }

    public void setBuyInvoiceApplyNumber(String buyInvoiceApplyNumber) {
        this.buyInvoiceApplyNumber = buyInvoiceApplyNumber;
    }

    public BigDecimal getBuyInvoiceAmount() {
        return buyInvoiceAmount;
    }

    public void setBuyInvoiceAmount(BigDecimal buyInvoiceAmount) {
        this.buyInvoiceAmount = buyInvoiceAmount;
    }

    public Integer getInvoicecCount() {
        return invoicecCount;
    }

    public void setInvoicecCount(Integer invoicecCount) {
        this.invoicecCount = invoicecCount;
    }

    public Long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }
}
