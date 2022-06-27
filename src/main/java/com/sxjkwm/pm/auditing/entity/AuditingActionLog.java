package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.*;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:20
 */
@Entity
@Table(name = "auditing_action_log")
public class AuditingActionLog extends BaseEntity {

    @Column(name = "auditing_record_id")
    private Long auditingRecordId;

    @Column(name = "operation_user_id")
    private String operationUserId;

    @Column(name = "auditing_action")
    private Integer auditingAction; // AuditingConstant

    @Column(name = "reject_comment", columnDefinition = "varchar(2000)")
    private String rejectComment;

    @Column(name = "auditing_flow_node_id")
    private Long auditingFlowNodeId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "esign")
    private byte[] eSign;  // 线上签字图片

    public Long getAuditingRecordId() {
        return auditingRecordId;
    }

    public void setAuditingRecordId(Long auditingRecordId) {
        this.auditingRecordId = auditingRecordId;
    }

    public String getOperationUserId() {
        return operationUserId;
    }

    public void setOperationUserId(String operationUserId) {
        this.operationUserId = operationUserId;
    }

    public Integer getAuditingAction() {
        return auditingAction;
    }

    public void setAuditingAction(Integer auditingAction) {
        this.auditingAction = auditingAction;
    }

    public String getRejectComment() {
        return rejectComment;
    }

    public void setRejectComment(String rejectComment) {
        this.rejectComment = rejectComment;
    }

    public Long getAuditingFlowNodeId() {
        return auditingFlowNodeId;
    }

    public void setAuditingFlowNodeId(Long auditingFlowNodeId) {
        this.auditingFlowNodeId = auditingFlowNodeId;
    }

    public byte[] geteSign() {
        return eSign;
    }

    public void seteSign(byte[] eSign) {
        this.eSign = eSign;
    }
}
