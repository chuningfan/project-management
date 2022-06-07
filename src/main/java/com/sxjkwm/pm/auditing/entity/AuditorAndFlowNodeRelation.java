package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/6/7 9:39
 */
@Entity
@Table(name = "auditor_and_flow_node_relation")
public class AuditorAndFlowNodeRelation extends BaseEntity {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "auditing_flow_node_id")
    private Long auditingFlowNodeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getAuditingFlowNodeId() {
        return auditingFlowNodeId;
    }

    public void setAuditingFlowNodeId(Long auditingFlowNodeId) {
        this.auditingFlowNodeId = auditingFlowNodeId;
    }
}
