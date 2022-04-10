package com.sxjkwm.pm.business.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_project_flow")
@Entity
public class ProjectFlow extends BaseEntity {

    @Column(name = "flow_name")
    private String flowName;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}
