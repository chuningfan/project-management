package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.common.BaseEntity;
import com.sxjkwm.pm.constants.Constant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/6/7 9:20
 */
@Entity
@Table(name = "auditing_flow_node")
public class AuditingFlowNode extends BaseEntity {

    @Column(name = "auditing_flow_id")
    private Long auditingFlowId;

    @Column(name = "node_index")
    private Integer nodeIndex;

    @Column(name = "auditing_status")
    private Integer auditingStatus = Constant.YesOrNo.NO.getValue();

    public Long getAuditingFlowId() {
        return auditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        this.auditingFlowId = auditingFlowId;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public Integer getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(Integer auditingStatus) {
        this.auditingStatus = auditingStatus;
    }
}
