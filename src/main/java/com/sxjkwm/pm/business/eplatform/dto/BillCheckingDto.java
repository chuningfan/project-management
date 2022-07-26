package com.sxjkwm.pm.business.eplatform.dto;

import org.apache.commons.collections4.MapUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/25 8:20
 */
public class BillCheckingDto implements Serializable {

    private String supplierName;

    private String buyOrderNo;

    private String buyBillNo;

    private BigDecimal buyBillAmount;

    private String buyBillStatus;

    private String buyInvoiceApplyNo;

    private BigDecimal buyInvoiceAmount;

    private String buyInvoiceType;

    private String buyInvoiceApplyStatus;

    private String buyInvoicePrintingStatus;

    private String buyInvoiceErrorMsg;

    private String buyerName;

    private Long orderTime;

    private String buyerOrgName;

    private String saleOrderNo;

    private BigDecimal saleFinalPrice;

    private String saleBillNo;

    private BigDecimal saleBillAmount;

    private String saleBillStatus;

    private String saleInvoiceApplyNo;

    private String saleInvoiceType;

    private BigDecimal saleInvoiceAmount;

    private String saleInvoiceApplyStatus;

    private String saleInvoicePrintingStatus;

    private String saleInvoiceErrorMsg;

    public BillCheckingDto() {
    }

    public static BillCheckingDto fillData(Map<String, Object> dataMap) {
        BillCheckingDto dto = new BillCheckingDto();
        dto.setSupplierName(MapUtils.getString(dataMap, "supplierName"));
        dto.setBuyOrderNo(MapUtils.getString(dataMap, "buyOrderNo"));
        dto.setBuyBillNo(MapUtils.getString(dataMap, "buyBillNo"));
        dto.setBuyBillAmount((BigDecimal) MapUtils.getNumber(dataMap, "buyBillAmount"));
        dto.setBuyBillStatus(MapUtils.getString(dataMap, "buyBillStatus"));
        dto.setBuyInvoiceApplyNo(MapUtils.getString(dataMap, "buyInvoiceApplyNo"));
        dto.setBuyInvoiceAmount((BigDecimal) MapUtils.getNumber(dataMap, "buyInvoiceAmount"));
        dto.setBuyInvoiceType(MapUtils.getString(dataMap, "buyInvoiceType"));
        dto.setBuyInvoiceApplyStatus(MapUtils.getString(dataMap, "buyInvoiceApplyStatus"));
        dto.setBuyInvoicePrintingStatus(MapUtils.getString(dataMap, "buyInvoicePrintingStatus"));
        dto.setBuyInvoiceErrorMsg(MapUtils.getString(dataMap, "buyInvoiceErrorMsg"));
        dto.setBuyerName(MapUtils.getString(dataMap, "buyerName"));
        dto.setOrderTime(((Date)MapUtils.getObject(dataMap, "orderTime")).getTime());
        dto.setBuyerOrgName(MapUtils.getString(dataMap, "buyerOrgName"));
        dto.setSaleOrderNo(MapUtils.getString(dataMap, "saleOrderNo"));
        dto.setSaleFinalPrice((BigDecimal) MapUtils.getNumber(dataMap, "saleFinalPrice"));
        dto.setSaleBillNo(MapUtils.getString(dataMap, "saleBillNo"));
        dto.setSaleBillAmount((BigDecimal) MapUtils.getNumber(dataMap, "saleBillAmount"));
        dto.setSaleBillStatus(MapUtils.getString(dataMap, "saleBillStatus"));
        dto.setSaleInvoiceApplyNo(MapUtils.getString(dataMap, "saleInvoiceApplyNo"));
        dto.setSaleInvoiceType(MapUtils.getString(dataMap, "saleInvoiceType"));
        dto.setSaleInvoiceAmount((BigDecimal) MapUtils.getNumber(dataMap, "saleInvoiceAmount"));
        dto.setSaleInvoiceApplyStatus(MapUtils.getString(dataMap, "saleInvoiceApplyStatus"));
        dto.setSaleInvoicePrintingStatus(MapUtils.getString(dataMap, "saleInvoicePrintingStatus"));
        dto.setSaleInvoiceErrorMsg(MapUtils.getString(dataMap, "saleInvoiceErrorMsg"));
        return dto;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getBuyOrderNo() {
        return buyOrderNo;
    }

    public void setBuyOrderNo(String buyOrderNo) {
        this.buyOrderNo = buyOrderNo;
    }

    public String getBuyBillNo() {
        return buyBillNo;
    }

    public void setBuyBillNo(String buyBillNo) {
        this.buyBillNo = buyBillNo;
    }

    public BigDecimal getBuyBillAmount() {
        return buyBillAmount;
    }

    public void setBuyBillAmount(BigDecimal buyBillAmount) {
        this.buyBillAmount = buyBillAmount;
    }

    public String getBuyBillStatus() {
        return buyBillStatus;
    }

    public void setBuyBillStatus(String buyBillStatus) {
        this.buyBillStatus = buyBillStatus;
    }

    public String getBuyInvoiceApplyNo() {
        return buyInvoiceApplyNo;
    }

    public void setBuyInvoiceApplyNo(String buyInvoiceApplyNo) {
        this.buyInvoiceApplyNo = buyInvoiceApplyNo;
    }

    public BigDecimal getBuyInvoiceAmount() {
        return buyInvoiceAmount;
    }

    public void setBuyInvoiceAmount(BigDecimal buyInvoiceAmount) {
        this.buyInvoiceAmount = buyInvoiceAmount;
    }

    public String getBuyInvoiceType() {
        return buyInvoiceType;
    }

    public void setBuyInvoiceType(String buyInvoiceType) {
        this.buyInvoiceType = buyInvoiceType;
    }

    public String getBuyInvoiceApplyStatus() {
        return buyInvoiceApplyStatus;
    }

    public void setBuyInvoiceApplyStatus(String buyInvoiceApplyStatus) {
        this.buyInvoiceApplyStatus = buyInvoiceApplyStatus;
    }

    public String getBuyInvoicePrintingStatus() {
        return buyInvoicePrintingStatus;
    }

    public void setBuyInvoicePrintingStatus(String buyInvoicePrintingStatus) {
        this.buyInvoicePrintingStatus = buyInvoicePrintingStatus;
    }

    public String getBuyInvoiceErrorMsg() {
        return buyInvoiceErrorMsg;
    }

    public void setBuyInvoiceErrorMsg(String buyInvoiceErrorMsg) {
        this.buyInvoiceErrorMsg = buyInvoiceErrorMsg;
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

    public String getSaleOrderNo() {
        return saleOrderNo;
    }

    public void setSaleOrderNo(String saleOrderNo) {
        this.saleOrderNo = saleOrderNo;
    }

    public BigDecimal getSaleFinalPrice() {
        return saleFinalPrice;
    }

    public void setSaleFinalPrice(BigDecimal saleFinalPrice) {
        this.saleFinalPrice = saleFinalPrice;
    }

    public String getSaleBillNo() {
        return saleBillNo;
    }

    public void setSaleBillNo(String saleBillNo) {
        this.saleBillNo = saleBillNo;
    }

    public BigDecimal getSaleBillAmount() {
        return saleBillAmount;
    }

    public void setSaleBillAmount(BigDecimal saleBillAmount) {
        this.saleBillAmount = saleBillAmount;
    }

    public String getSaleBillStatus() {
        return saleBillStatus;
    }

    public void setSaleBillStatus(String saleBillStatus) {
        this.saleBillStatus = saleBillStatus;
    }

    public String getSaleInvoiceApplyNo() {
        return saleInvoiceApplyNo;
    }

    public void setSaleInvoiceApplyNo(String saleInvoiceApplyNo) {
        this.saleInvoiceApplyNo = saleInvoiceApplyNo;
    }

    public String getSaleInvoiceType() {
        return saleInvoiceType;
    }

    public void setSaleInvoiceType(String saleInvoiceType) {
        this.saleInvoiceType = saleInvoiceType;
    }

    public BigDecimal getSaleInvoiceAmount() {
        return saleInvoiceAmount;
    }

    public void setSaleInvoiceAmount(BigDecimal saleInvoiceAmount) {
        this.saleInvoiceAmount = saleInvoiceAmount;
    }

    public String getSaleInvoiceApplyStatus() {
        return saleInvoiceApplyStatus;
    }

    public void setSaleInvoiceApplyStatus(String saleInvoiceApplyStatus) {
        this.saleInvoiceApplyStatus = saleInvoiceApplyStatus;
    }

    public String getSaleInvoicePrintingStatus() {
        return saleInvoicePrintingStatus;
    }

    public void setSaleInvoicePrintingStatus(String saleInvoicePrintingStatus) {
        this.saleInvoicePrintingStatus = saleInvoicePrintingStatus;
    }

    public String getSaleInvoiceErrorMsg() {
        return saleInvoiceErrorMsg;
    }

    public void setSaleInvoiceErrorMsg(String saleInvoiceErrorMsg) {
        this.saleInvoiceErrorMsg = saleInvoiceErrorMsg;
    }
}
