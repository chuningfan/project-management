package com.sxjkwm.pm.business.eplatform.dto;

import com.sxjkwm.pm.common.PageRequest;

/**
 * @author Vic.Chu
 * @date 2022/8/24 8:53
 */
public class OutboundQueryParam extends PageRequest {

    private String buyerOrgs;

    private String invoiceTitle;

    private String invoiceApplyNum;

    private String supplierNames;

    public String getBuyerOrgs() {
        return buyerOrgs;
    }

    public void setBuyerOrgs(String buyerOrgs) {
        this.buyerOrgs = buyerOrgs;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceApplyNum() {
        return invoiceApplyNum;
    }

    public void setInvoiceApplyNum(String invoiceApplyNum) {
        this.invoiceApplyNum = invoiceApplyNum;
    }

    public String getSupplierNames() {
        return supplierNames;
    }

    public void setSupplierNames(String supplierNames) {
        this.supplierNames = supplierNames;
    }
}
