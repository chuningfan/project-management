package com.sxjkwm.pm.business.finance.entity;

import com.sxjkwm.pm.common.BaseCollectionProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/5/16 19:46
 */
@Entity
@Table(name = "pm_order_item")
public class OrderItem extends BaseCollectionProperty {

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "item_model")
    private String itemModel;

    @Column(name = "buy_price")
    private BigDecimal buyPrice;

    @Column(name = "buy_count")
    private BigDecimal buyCount;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    @Column(name = "sell_count")
    private BigDecimal sellCount;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "buy_no_tax_amount")
    private BigDecimal buyNoTaxAmount;

    @Column(name = "buy_tax_amount")
    private BigDecimal buyTaxAmount;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getItemModel() {
        return itemModel;
    }

    public void setItemModel(String itemModel) {
        this.itemModel = itemModel;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(BigDecimal buyCount) {
        this.buyCount = buyCount;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellCount() {
        return sellCount;
    }

    public void setSellCount(BigDecimal sellCount) {
        this.sellCount = sellCount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getBuyTaxAmount() {
        return buyTaxAmount;
    }

    public void setBuyTaxAmount(BigDecimal buyTaxAmount) {
        this.buyTaxAmount = buyTaxAmount;
    }

    public BigDecimal getBuyNoTaxAmount() {
        return buyNoTaxAmount;
    }

    public void setBuyNoTaxAmount(BigDecimal buyNoTaxAmount) {
        this.buyNoTaxAmount = buyNoTaxAmount;
    }

}
