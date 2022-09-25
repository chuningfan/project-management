package com.sxjkwm.pm.thirdparty.do1.entitiy;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/9/17 15:49
 */
@Entity
@Table(name = "pm_do1_property_mapping", indexes = {@Index(name = "pmDo1_property_mapping_fnId", columnList = "auditing_flow_node_id")})
public class PropertyMapping extends BaseEntity {

    @Column(name = "do1_form_id")
    private String formId;

    @Column(name = "field_id")
    private String fieldId;

    @Column(name = "def_id")
    private Long defId;

    @Column(name = "def_flow_node_id")
    private Long defFlowNodeId;

    @Column(name = "auditing_flow_node_id")
    private Long auditingFlowNodeId;

    @Column(name = "flow_id")
    private Long flowId;

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
