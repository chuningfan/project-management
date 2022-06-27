package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/6/7 9:19
 */
@Entity
@Table(name = "auditing_flow")
public class AuditingFlow extends BaseEntity {

    @Column(name = "auditing_flow_name")
    private String name;

    @Column(name = "description", columnDefinition = "varchar(2000)")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
