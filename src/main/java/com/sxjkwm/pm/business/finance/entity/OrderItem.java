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

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "buy_price")
    private BigDecimal buyPrice;

    @Column(name = "buy_count")
    private Integer buyCount;

    @Column(name = "tax_value")
    private BigDecimal taxValue;

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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public BigDecimal getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(BigDecimal taxValue) {
        this.taxValue = taxValue;
    }
}
