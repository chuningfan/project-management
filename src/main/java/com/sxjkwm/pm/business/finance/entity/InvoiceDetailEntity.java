package com.sxjkwm.pm.business.finance.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_invoice_detail")
public class InvoiceDetailEntity extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "goods_code")
    private String goodsCode;

    @Column(name = "with_tax_flag")
    private String withTaxFlag; // 单价含税标志：0:不含税,1:含税

    @Column(name = "goods_price")
    private String price;

    @Column(name = "goods_num")
    private String num;

    @Column(name = "goods_unit")
    private String unit;

    @Column(name = "spec_type")
    private String specType;

    @Column(name = "tax")
    private String tax; // 税额

    @Column(name = "tax_rate")
    private String taxRate = "0.13";

    /**
     * 不含税金额。红票为负。不含税金额、税
     * 额、含税金额任何一个不传时，会根据传
     * 入的单价，数量进行计算，可能和实际数
     * 值存在误差，建议都传入
     */
    @Column(name = "tax_excluded_amount")
    private String taxExcludedAmount;

    /**
     * 含税金额，[不含税金额] + [税额] = [含税
     * 金额]，红票为负。不含税金额、税额、含
     * 税金额任何一个不传时，会根据传入的单
     * 价，数量进行计算，可能和实际数值存在
     * 误差，建议都传入
     */
    @Column(name = "tax_included_amount")
    private String taxIncludedAmount;

    @Column(name = "invoice_line_property")
    private String invoiceLineProperty = "0";

    @Column(name = "favoured_policy_flag")
    private String favouredPolicyFlag = "0";

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getWithTaxFlag() {
        return withTaxFlag;
    }

    public void setWithTaxFlag(String withTaxFlag) {
        this.withTaxFlag = withTaxFlag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpecType() {
        return specType;
    }

    public void setSpecType(String specType) {
        this.specType = specType;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxExcludedAmount() {
        return taxExcludedAmount;
    }

    public void setTaxExcludedAmount(String taxExcludedAmount) {
        this.taxExcludedAmount = taxExcludedAmount;
    }

    public String getTaxIncludedAmount() {
        return taxIncludedAmount;
    }

    public void setTaxIncludedAmount(String taxIncludedAmount) {
        this.taxIncludedAmount = taxIncludedAmount;
    }

    public String getInvoiceLineProperty() {
        return invoiceLineProperty;
    }

    public void setInvoiceLineProperty(String invoiceLineProperty) {
        this.invoiceLineProperty = invoiceLineProperty;
    }

    public String getFavouredPolicyFlag() {
        return favouredPolicyFlag;
    }

    public void setFavouredPolicyFlag(String favouredPolicyFlag) {
        this.favouredPolicyFlag = favouredPolicyFlag;
    }
}
