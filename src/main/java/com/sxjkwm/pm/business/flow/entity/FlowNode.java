package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.common.BaseFlowNode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Table(name = "pm_flow_node", indexes = {@Index(name = "flownode_flowId", columnList = "flow_id")})
@Entity
public class FlowNode extends BaseFlowNode {

    @Column(name="description", length = 4000)
    private String description;

    @Column(name = "flow_node_value")
    private String flowNodeValue; // This is for s3 file bucket

    public FlowNode(Long flowId, FlowNodeDto dto) {
        this.flowId = flowId;
        this.nodeName = dto.getNodeName();
        this.nodeIndex = dto.getNodeIndex();
        this.description = dto.getDescription();
        this.skippable = dto.getSkippable();
        this.formId = dto.getFormId();
        this.flowNodeValue = dto.getFlowNodeValue();
        this.id = dto.getId();
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
}
