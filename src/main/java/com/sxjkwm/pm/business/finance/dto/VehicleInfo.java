package com.sxjkwm.pm.business.finance.dto;

import java.io.Serializable;

public class VehicleInfo implements Serializable {

    /**
     * 车辆类型,同明细中商品名称，开具机动车
     * 发票时明细有且仅有一行，商品名称为车
     * 辆类型且不能为空
     */
    private String vehicleType;

    private String brandModel; // 厂牌型号

    private String productOrigin; // 原产地

    private String certificate; // 合格证号

    private String importCerNum; // 进出口证明书号

    private String insOddNum; // 商检单号

    private String engineNum; // 发动机号码

    private String vehicleCode; // 车辆识别号码/车架号

    private String intactCerNum; // 完税证明号码

    private String tonnage; // 吨位

    private String maxCapacity; // 限乘人数

    /**
     * 身份证号码或组织机构代码；该字段为空
     * 则为2021新版机动车发票，此时购方税号
     * 必填（个人填身份证）；该字段有值，则
     * 为老版本机动车发票
     */
    private String idNumOrgCode;

    private String manufacturerName;

    private String taxOfficeName;

    private String taxOfficeCode;

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getProductOrigin() {
        return productOrigin;
    }

    public void setProductOrigin(String productOrigin) {
        this.productOrigin = productOrigin;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getImportCerNum() {
        return importCerNum;
    }

    public void setImportCerNum(String importCerNum) {
        this.importCerNum = importCerNum;
    }

    public String getInsOddNum() {
        return insOddNum;
    }

    public void setInsOddNum(String insOddNum) {
        this.insOddNum = insOddNum;
    }

    public String getEngineNum() {
        return engineNum;
    }

    public void setEngineNum(String engineNum) {
        this.engineNum = engineNum;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getIntactCerNum() {
        return intactCerNum;
    }

    public void setIntactCerNum(String intactCerNum) {
        this.intactCerNum = intactCerNum;
    }

    public String getTonnage() {
        return tonnage;
    }

    public void setTonnage(String tonnage) {
        this.tonnage = tonnage;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getIdNumOrgCode() {
        return idNumOrgCode;
    }

    public void setIdNumOrgCode(String idNumOrgCode) {
        this.idNumOrgCode = idNumOrgCode;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getTaxOfficeName() {
        return taxOfficeName;
    }

    public void setTaxOfficeName(String taxOfficeName) {
        this.taxOfficeName = taxOfficeName;
    }

    public String getTaxOfficeCode() {
        return taxOfficeCode;
    }

    public void setTaxOfficeCode(String taxOfficeCode) {
        this.taxOfficeCode = taxOfficeCode;
    }
}
