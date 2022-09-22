package com.sxjkwm.pm.business.flow.dto;

import java.io.Serializable;

public class FlowNodeDto implements Serializable {

    private Long id;

    private String nodeName;

    private Integer isDeleted;

    private Integer skippable;

    private Integer nodeIndex;

    private Integer flowVersion;

    private String description;

    private String flowNodeValue;

    private Integer needAudit;

    private Long auditingFlowId;

    private String formId;

    private String specialFormDataHandler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSkippable() {
        return skippable;
    }

    public void setSkippable(Integer skippable) {
        this.skippable = skippable;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFlowNodeValue() {
        return flowNodeValue;
    }

    public void setFlowNodeValue(String flowNodeValue) {
        this.flowNodeValue = flowNodeValue;
    }

    public Integer getNeedAudit() {
        return needAudit;
    }

    public void setNeedAudit(Integer needAudit) {
        this.needAudit = needAudit;
    }

    public Long getAuditingFlowId() {
        return auditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        this.auditingFlowId = auditingFlowId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getSpecialFormDataHandler() {
        return specialFormDataHandler;
    }

    public void setSpecialFormDataHandler(String specialFormDataHandler) {
        this.specialFormDataHandler = specialFormDataHandler;
    }
}
