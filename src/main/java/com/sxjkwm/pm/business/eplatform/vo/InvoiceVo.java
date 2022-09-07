package com.sxjkwm.pm.business.eplatform.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName InvoiceVo
 * @Description
 * @Author wubin
 * @Date 2022/8/26 10:40
 * @Version 1.0
 **/
public class InvoiceVo {

    /**
     * 发票申请单
     */
    private String invoiceCode;
    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;
    /**
     * 发票抬头
     */
    private String invoiceTitle;
    /**
     * 采购单位
     */
    private String buyerOrg;


    /**
     * 供应商金额信息
     */
    private List<SupplierInfoVo> supplierInfo;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getBuyerOrg() {
        return buyerOrg;
    }

    public void setBuyerOrg(String buyerOrg) {
        this.buyerOrg = buyerOrg;
    }

    public List<SupplierInfoVo> getSupplierInfo() {
        return supplierInfo;
    }

    public void setSupplierInfo(List<SupplierInfoVo> supplierInfo) {
        this.supplierInfo = supplierInfo;
    }
}
