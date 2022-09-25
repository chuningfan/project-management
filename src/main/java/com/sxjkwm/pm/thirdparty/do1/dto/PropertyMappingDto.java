package com.sxjkwm.pm.thirdparty.do1.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/9/17 16:00
 */
public class PropertyMappingDto implements Serializable {

    private Long id;

    private String formId;

    private String fieldId;

    private Long defId;

    private Long defFlowNodeId;

    private Long auditingFlowNodeId;

    private Long flowId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public Long getDefId() {
        return defId;
    }

    public void setDefId(Long defId) {
        this.defId = defId;
    }

    public Long getDefFlowNodeId() {
        return defFlowNodeId;
    }

    public void setDefFlowNodeId(Long defFlowNodeId) {
        this.defFlowNodeId = defFlowNodeId;
    }

    public Long getAuditingFlowNodeId() {
        return auditingFlowNodeId;
    }

    public void setAuditingFlowNodeId(Long auditingFlowNodeId) {
        this.auditingFlowNodeId = auditingFlowNodeId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }
}
