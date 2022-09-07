package com.sxjkwm.pm.business.eplatform.dto;

import java.math.BigDecimal;

/**
 * @ClassName InvoiceDto
 * @Description
 * @Author wubin
 * @Date 2022/8/26 10:32
 * @Version 1.0
 **/
public class InvoiceDto {

    /**
     * 发票申请单
     */
    private String applyNumber;
    /**
     * 采购单位
     */
    private String organizeName;
    /**
     * 供应商名称
     */
    private String shopName;
    /**
     * 发票抬头
     */
    private String invoiceTitle;
    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;


    private Integer pageNum;

    private Integer pageSize;

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(String applyNumber) {
        this.applyNumber = applyNumber;
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }
}
