package com.sxjkwm.pm.business.finance.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 暂不实现
 */
@Entity
@Table(name = "pm_secondhand_car_info")
public class SecondHandCarInfoEntity extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "organize_type")
    private String organizeType;

    @Column(name = "vehicle_type")
    private String vehicleType;

    @Column(name = "brand_model")
    private String brandModel;

    @Column(name = "vehicle_code")
    private String vehicleCode;

    @Column(name = "intact_cer_num")
    private String intactCerNum;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "register_cer_no")
    private String registerCertNo;

    @Column(name = "vehicle_management_name")
    private String vehicleManagementName;

    @Column(name = "seller_name")
    private String sellerName;

    @Column(name = "seller_tax_num")
    private String sellerTaxnum;

    @Column(name = "seller_address")
    private String sellerAddress;

    @Column(name = "seller_phone")
    private String sellerPhone;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrganizeType() {
        return organizeType;
    }

    public void setOrganizeType(String organizeType) {
        this.organizeType = organizeType;
    }

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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getRegisterCertNo() {
        return registerCertNo;
    }

    public void setRegisterCertNo(String registerCertNo) {
        this.registerCertNo = registerCertNo;
    }

    public String getVehicleManagementName() {
        return vehicleManagementName;
    }

    public void setVehicleManagementName(String vehicleManagementName) {
        this.vehicleManagementName = vehicleManagementName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxnum() {
        return sellerTaxnum;
    }

    public void setSellerTaxnum(String sellerTaxnum) {
        this.sellerTaxnum = sellerTaxnum;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }
}
