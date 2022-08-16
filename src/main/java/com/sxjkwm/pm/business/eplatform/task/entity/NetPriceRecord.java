package com.sxjkwm.pm.business.eplatform.task.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Vic.Chu
 * @date 2022/8/15 9:04
 * 价格浮动类商品网价拉取记录
 *
 */
@Entity
@Table(name = "ep_net_price_record")
public class NetPriceRecord extends BaseEntity {

    @Column(name = "record_time")
    private Long recordTime;

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
