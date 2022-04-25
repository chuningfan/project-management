package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_flow")
@Entity
public class Flow extends BaseEntity {

    @Column(name = "flow_name")
    private String flowName;

    @Column(name = "project_description")
    private String description;

    @Column(name = "flow_version")
    private Integer version;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
