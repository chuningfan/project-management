package com.sxjkwm.pm.business.finance.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_order")
public class OrderEntity extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "task_num")
    private String taskNum;

    @Column(name = "buyer_name")
    private String buyerName; //企业名称/个人 购方名称

    @Column(name = "buyer_tax_num")
    private String  buyerTaxNum;

    @Column(name = "buyer_tel")
    private String buyerTel;

    @Column(name = "buyer_address")
    private String buyerAddress;

    @Column(name = "buyer_account")
    private String buyerAccount;

    @Column(name = "saler_tax_num")
    private String salerTaxNum;

    @Column(name = "saler_tel")
    private String salerTel;

    @Column(name = "saler_address")
    private String salerAddress;

    @Column(name = "saler_account")
    private String salerAccount;

    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "invoice_date")
    private String invoiceDate;

    @Column(name = "invoice_code")
    private String invoiceCode; // 冲红使用

    @Column(name = "invoice_num")
    private String invoiceNum; // 冲红发票时使用

    @Column(name = "bill_info_no")
    private String billInfoNo; // 冲红必填

    @Column(name = "remark")
    private String remark; // 冲红使用

    @Column(name = "checker")
    private String checker; // 复核人

    @Column(name = "payee")
    private String payee; // 收款人

    @Column(name = "clerk")
    private String clerk; // 开票人

    @Column(name = "list_flag")
    private String listFlag = "1"; // 是否清单

    @Column(name = "list_name")
    private String listName; // 清单名称

    @Column(name = "push_mode")
    private String pushMode = "-1";

    @Column(name = "invoice_type")
    private String invoiceType = "1";

    /**
     * 发票种类：p,普通发票(电票)(默认);c,普通
     * 发票(纸票);s,专用发票;e,收购发票(电票);f,
     * 收购发票(纸质);r,普通发票(卷式);b,增值税
     * 电子专用发票;j,机动车销售统一发票;u,二手
     * 车销售统一发票
     */
    @Column(name = "invoice_line")
    private String invoiceLine;

    @Column(name = "product_oil_flag")
    private String productOilFlag; // 成品油标志：非成品油(默认):0;成品油:1

    @Column(name = "vehicle_flag")
    private String vehicleFlag; // 是否机动车类专票 0-否 1-是

    @Column(name = "order_check_status")
    private Integer orderCheckStatus; // 财务人员审批状态 0：初始；1:一审通过；2：二审通过

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

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

    public String getVehicleFlag() {
        return vehicleFlag;
    }

    public void setVehicleFlag(String vehicleFlag) {
        this.vehicleFlag = vehicleFlag;
    }
}
