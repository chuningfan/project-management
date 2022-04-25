package com.sxjkwm.pm.business.flow.dto;

import com.sxjkwm.pm.constants.Constant;

import java.io.Serializable;

public class FlowDto implements Serializable {

    private Long id;

    private String flowName;

    private String description;

    private Integer version;

    private Integer isDeleted = Constant.YesOrNo.NO.getValue();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
