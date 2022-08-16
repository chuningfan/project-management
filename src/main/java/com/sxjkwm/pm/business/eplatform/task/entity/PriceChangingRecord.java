package com.sxjkwm.pm.business.eplatform.task.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/8/15 9:03
 * 价格浮动类商品价格变动记录
 */
@Entity
@Table(name = "ep_price_changing_record")
public class PriceChangingRecord extends BaseEntity {

    @Column(name = "record_time")
    private Long recordTime;

    @Column(name = "previous_price")
    private BigDecimal previousPrice;

    @Column(name = "timed_price")
    private BigDecimal timedPrice;

    @Column(name = "material_category")
    private String materialCategory;

    public Long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(BigDecimal previousPrice) {
        this.previousPrice = previousPrice;
    }

    public BigDecimal getTimedPrice() {
        return timedPrice;
    }

    public void setTimedPrice(BigDecimal timedPrice) {
        this.timedPrice = timedPrice;
    }

    public String getMaterialCategory() {
        return materialCategory;
    }

    public void setMaterialCategory(String materialCategory) {
        this.materialCategory = materialCategory;
    }
}
