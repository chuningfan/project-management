package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/6/7 17:53
 */
@Entity
@Table(name = "auditor_group")
public class AuditorGroup extends BaseEntity {

    @Column(name = "user_ids", columnDefinition = "varchar(2000)")
    private String userIds; // 此处为【或签】关系

    @Column(name = "auditing_flow_node_id")
    private Long auditingFlowNodeId;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Long getAuditingFlowNodeId() {
        return auditingFlowNodeId;
    }

    public void setAuditingFlowNodeId(Long auditingFlowNodeId) {
        this.auditingFlowNodeId = auditingFlowNodeId;
    }
}
