package com.sxjkwm.pm.business.finance.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_order_ops_record")
public class OperationRecord extends BaseEntity {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "ops_type")
    private Integer opsType;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOpsType() {
        return opsType;
    }

    public void setOpsType(Integer opsType) {
        this.opsType = opsType;
    }
}
