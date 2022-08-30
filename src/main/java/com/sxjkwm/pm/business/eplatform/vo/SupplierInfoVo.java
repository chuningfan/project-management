package com.sxjkwm.pm.business.eplatform.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName supplierInfoVo
 * @Description
 * @Author wubin
 * @Date 2022/8/26 10:50
 * @Version 1.0
 **/
public class SupplierInfoVo {

    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 供应商金额
     */
    private BigDecimal supplierAmount;

    public SupplierInfoVo(String supplierName, BigDecimal supplierAmount, String orderNo) {
        this.supplierName = supplierName;
        this.supplierAmount = supplierAmount;
        this.orderNo = orderNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getSupplierAmount() {
        return supplierAmount;
    }

    public void setSupplierAmount(BigDecimal supplierAmount) {
        this.supplierAmount = supplierAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
