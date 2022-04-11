package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.common.BaseFlowNode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_flow_node")
@Entity
public class FlowNode extends BaseFlowNode {

    @Column(name="description")
    private String description;

    public FlowNode(Long flowId, FlowNodeDto dto) {
        this.flowId = flowId;
        this.nodeVersion = dto.getNodeVersion();
        this.nodeName = dto.getNodeName();
        this.nodeIndex = dto.getNodeIndex();
        this.description = dto.getDescription();
        this.skippable = dto.getSkippable();
        this.audittable = dto.getAudittable();
        this.formId = dto.getFormId();
    }

    public FlowNode() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
