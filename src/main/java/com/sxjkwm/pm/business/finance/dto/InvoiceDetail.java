package com.sxjkwm.pm.business.finance.dto;

import java.io.Serializable;

/**
 * 由业务人员填写单据数据
 *
 */
public class InvoiceDetail implements Serializable {

    private String goodsName; // 商品名称

    private String goodsCode; // 税收编码

    private String withTaxFlag = "1"; // 单价含税标志：0:不含税,1:含税

    /**
     * 单价（精确到小数点后8位），当单价(pri
     * ce)为空时，数量(num)也必须为空；(pric
     * e)为空时，含税金额(taxIncludedAmou
     * nt)、不含税金额(taxExcludedAmount)
     * 、税额(tax)都不能为空
     */
    private String price;

    private String num; // 数量

    private String unit; // 单位

    private String specType; // 型号

    private String tax; // 税额

    private String taxRate = "0.13"; // 税率

    /**
     * 不含税金额。红票为负。不含税金额、税
     * 额、含税金额任何一个不传时，会根据传
     * 入的单价，数量进行计算，可能和实际数
     * 值存在误差，建议都传入
     */
    private String taxExcludedAmount;

    /**
     * 含税金额，[不含税金额] + [税额] = [含税
     * 金额]，红票为负。不含税金额、税额、含
     * 税金额任何一个不传时，会根据传入的单
     * 价，数量进行计算，可能和实际数值存在
     * 误差，建议都传入
     */
    private String taxIncludedAmount;

    private String invoiceLineProperty = "0";

    private String favouredPolicyFlag = "0";

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
