package com.sxjkwm.pm.business.file.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseFile extends BaseEntity {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "property_def_id")
    private Long propertyDefId;

    @Column(name = "file_suffix")
    private String suffix;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getPropertyDefId() {
        return propertyDefId;
    }

    public void setPropertyDefId(Long propertyDefId) {
        this.propertyDefId = propertyDefId;
    }
}
