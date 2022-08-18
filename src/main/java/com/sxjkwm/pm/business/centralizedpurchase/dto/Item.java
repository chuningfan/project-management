package com.sxjkwm.pm.business.centralizedpurchase.dto;

import org.apache.commons.collections4.MapUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/8/17 9:08
 */
public class Item {

    private String itemName;

    private String materialName;

    private String specification;

    private String unit;

    private String quantity;

    private String brand;

    private String remark;

    private String materialClassName;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private BigDecimal taxRate;

    private BigDecimal tax;

    private BigDecimal cost;

    private String supplierName;

    private Long winningDate;

    private String supplierContactPerson;

    private String supplierMobile;

    public Item(Map<String, Object> dataMap) {
        this.itemName = MapUtils.getString(dataMap, "itemname");
        this.materialName = MapUtils.getString(dataMap, "materialname");
        this.specification = MapUtils.getString(dataMap, "specification");
        this.unit = MapUtils.getString(dataMap, "unit");
        this.quantity = MapUtils.getNumber(dataMap, "planquantity").toString();
        this.brand = MapUtils.getString(dataMap, "brand");
        this.remark = MapUtils.getString(dataMap, "remark");
        this.materialClassName = MapUtils.getString(dataMap, "materialclassname");
        this.unitPrice = (BigDecimal) MapUtils.getNumber(dataMap, "unitprice");
        this.totalPrice = (BigDecimal) MapUtils.getNumber(dataMap, "totalprice");
        this.taxRate = (BigDecimal) MapUtils.getNumber(dataMap, "taxrate");
        this.tax = (BigDecimal) MapUtils.getNumber(dataMap, "tax");
        this.cost = (BigDecimal) MapUtils.getNumber(dataMap, "cost");
        this.supplierName = MapUtils.getString(dataMap, "suppliername");
        Object winningDateObj = MapUtils.getObject(dataMap, "winningdate");
        this.winningDate = Objects.isNull(winningDateObj) ? null : ((Date) winningDateObj).getTime();
        this.supplierContactPerson = MapUtils.getString(dataMap, "suppliercontactperson");
        this.supplierMobile = MapUtils.getString(dataMap, "suppliermobile");
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMaterialClassName() {
        return materialClassName;
    }

    public void setMaterialClassName(String materialClassName) {
        this.materialClassName = materialClassName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Long getWinningDate() {
        return winningDate;
    }

    public void setWinningDate(Long winningDate) {
        this.winningDate = winningDate;
    }

    public String getSupplierContactPerson() {
        return supplierContactPerson;
    }

    public void setSupplierContactPerson(String supplierContactPerson) {
        this.supplierContactPerson = supplierContactPerson;
    }

    public String getSupplierMobile() {
        return supplierMobile;
    }

    public void setSupplierMobile(String supplierMobile) {
        this.supplierMobile = supplierMobile;
    }
}
