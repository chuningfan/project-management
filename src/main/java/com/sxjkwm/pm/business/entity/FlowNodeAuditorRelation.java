package com.sxjkwm.pm.business.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_flow_node_auditor_relation")
@Entity
public class FlowNodeAuditorRelation extends BaseEntity {

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name="user_id")
    private String userId;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
