package com.sxjkwm.pm.business.finance.dto;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    private String buyerName; //企业名称/个人 购方名称

    private String  buyerTaxNum;

    private String buyerTel;

    private String buyerAddress;

    private String buyerAccount;

    private String salerTaxNum;

    private String salerTel;

    private String salerAddress;

    private String salerAccount;

    private String orderNo;

    private String invoiceDate;

    private String invoiceCode; // 冲红使用

    private String invoiceNum; // 冲红发票时使用

    private String billInfoNo; // 冲红必填

    private String remark; // 冲红使用

    private String checker; // 复核人

    private String payee; // 收款人

    private String clerk; // 开票人

    private String listFlag; // 是否清单

    private String listName; // 清单名称

    private String pushMode = "-1"; // 推送信息 -1：不推送

    private String invoiceType = "1"; // 开票类型：1:蓝票;2:红票

    /**
     * 发票种类：p,普通发票(电票)(默认);c,普通
     * 发票(纸票);s,专用发票;e,收购发票(电票);f,
     * 收购发票(纸质);r,普通发票(卷式);b,增值税
     * 电子专用发票;j,机动车销售统一发票;u,二手
     * 车销售统一发票
     */
    private String invoiceLine = "s";

    private String productOilFlag; // 成品油标志：非成品油(默认):0;成品油:1

    private String callBackUrl; // 回传发票信息地址（开票完成、开票失败、作废成功、作废失败）

    private String vehicleFlag; // 是否机动车类专票 0-否 1-是

    private List<InvoiceDetail> invoiceDetail;

    private List<VehicleInfo> vehicleInfo;

    private List<SecondHandCarInfo> secondHandCarInfo;

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxNum() {
        return buyerTaxNum;
    }

    public void setBuyerTaxNum(String buyerTaxNum) {
        this.buyerTaxNum = buyerTaxNum;
    }

    public String getBuyerTel() {
        return buyerTel;
    }

    public void setBuyerTel(String buyerTel) {
        this.buyerTel = buyerTel;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getSalerTaxNum() {
        return salerTaxNum;
    }

    public void setSalerTaxNum(String salerTaxNum) {
        this.salerTaxNum = salerTaxNum;
    }

    public String getSalerTel() {
        return salerTel;
    }

    public void setSalerTel(String salerTel) {
        this.salerTel = salerTel;
    }

    public String getSalerAddress() {
        return salerAddress;
    }

    public void setSalerAddress(String salerAddress) {
        this.salerAddress = salerAddress;
    }

    public String getSalerAccount() {
        return salerAccount;
    }

    public void setSalerAccount(String salerAccount) {
        this.salerAccount = salerAccount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getBillInfoNo() {
        return billInfoNo;
    }

    public void setBillInfoNo(String billInfoNo) {
        this.billInfoNo = billInfoNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getClerk() {
        return clerk;
    }

    public void setClerk(String clerk) {
        this.clerk = clerk;
    }

    public String getListFlag() {
        return listFlag;
    }

    public void setListFlag(String listFlag) {
        this.listFlag = listFlag;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getPushMode() {
        return pushMode;
    }

    public void setPushMode(String pushMode) {
        this.pushMode = pushMode;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceLine() {
        return invoiceLine;
    }

    public void setInvoiceLine(String invoiceLine) {
        this.invoiceLine = invoiceLine;
    }

    public String getProductOilFlag() {
        return productOilFlag;
    }

    public void setProductOilFlag(String productOilFlag) {
        this.productOilFlag = productOilFlag;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getVehicleFlag() {
        return vehicleFlag;
    }

    public void setVehicleFlag(String vehicleFlag) {
        this.vehicleFlag = vehicleFlag;
    }

    public List<InvoiceDetail> getInvoiceDetail() {
        return invoiceDetail;
    }

    public void setInvoiceDetail(List<InvoiceDetail> invoiceDetail) {
        this.invoiceDetail = invoiceDetail;
    }

    public List<VehicleInfo> getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(List<VehicleInfo> vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public List<SecondHandCarInfo> getSecondHandCarInfo() {
        return secondHandCarInfo;
    }

    public void setSecondHandCarInfo(List<SecondHandCarInfo> secondHandCarInfo) {
        this.secondHandCarInfo = secondHandCarInfo;
    }
}
