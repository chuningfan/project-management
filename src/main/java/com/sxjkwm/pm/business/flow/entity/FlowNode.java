package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.common.BaseFlowNode;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "pm_flow_node", indexes = {@Index(name = "flownode_flowId", columnList = "flow_id")})
@Entity
@Proxy(lazy = false)
public class FlowNode extends BaseFlowNode {

    @Column(name="description", length = 4000)
    private String description;

    @Column(name = "flow_node_value")
    private String flowNodeValue; // This is for s3 file bucket

    @Column(name = "form_id")
    private String formId;

    @Column(name = "special_form_data_handler")
    private String specialFormDataHandler;

    public FlowNode(Long flowId, FlowNodeDto dto) {
        this.flowId = flowId;
        this.nodeName = dto.getNodeName();
        this.nodeIndex = dto.getNodeIndex();
        this.description = dto.getDescription();
        this.skippable = dto.getSkippable();
        this.flowNodeValue = dto.getFlowNodeValue();
        this.id = dto.getId();
        this.needAudit = dto.getNeedAudit();
        this.auditingFlowId = dto.getAuditingFlowId();
        this.formId = dto.getFormId();
        this.specialFormDataHandler = dto.getSpecialFormDataHandler();
    }

    public FlowNode() {
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
