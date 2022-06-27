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

//    @Column(name = "flowValue")
//    private String flowValue;  // This is for S3 file service to create bucket

    @Column(name = "data_type")
    private Integer dataType;

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

//    public String getFlowValue() {
//        return flowValue;
//    }
//
//    public void setFlowValue(String flowValue) {
//        this.flowValue = flowValue;
//    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }
}
