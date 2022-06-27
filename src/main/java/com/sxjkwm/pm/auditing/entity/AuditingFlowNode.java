package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.auditing.dto.AuditingFlowNodeDto;
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

    @Column(name = "auditing_step")
    private Integer auditingStep;

    @Column(name = "auditing_type")
    private Integer auditingType;

    public AuditingFlowNode(AuditingFlowNodeDto auditingFlowNodeDto) {
        this.id = auditingFlowNodeDto.getId();
        this.auditingFlowId = auditingFlowNodeDto.getAuditingFlowId();
        this.auditingStep = auditingFlowNodeDto.getStep();
        this.auditingType = auditingFlowNodeDto.getAuditingType();
    }

    public AuditingFlowNode() {
    }

    public Long getAuditingFlowId() {
        return auditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        this.auditingFlowId = auditingFlowId;
    }

    public Integer getAuditingStep() {
        return auditingStep;
    }

    public void setAuditingStep(Integer auditingStep) {
        this.auditingStep = auditingStep;
    }

    public Integer getAuditingType() {
        return auditingType;
    }

    public void setAuditingType(Integer auditingType) {
        this.auditingType = auditingType;
    }
}
