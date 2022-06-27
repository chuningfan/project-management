package com.sxjkwm.pm.auditing.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/6/7 17:55
 */
@Entity
@Table(name = "auditing_record")
public class AuditingRecord extends BaseEntity {

    @Column(name = "data_id")
    private Long dataId; // 被审批的数据主体ID,根据ID查询关联业务流中需要被审批的字段进行展示

    @Column(name = "data_type")
    private Integer dataType; // 被审批数据主体类型，对应constant中AuditingDataType

    @Column(name = "auditing_flow_id")
    private Long AuditingFlowId; // 关联审批流ID

    @Column(name = "current_auditing_step")
    private Integer currentAuditingStep;  // 当前审批步骤 对应AuditingFlowNode中的auditingStep，每次审批应更新该字段并通知发起人

    @Column(name = "auditing_status")
    private Integer auditingStatus; // 审批状态，AuditingConstant

    @Column(name = "user_id")
    private String userId; // 发起人userID

    @Column(name = "auditing_data", columnDefinition = "TEXT")
    public String auditingData;

    @Column(name = "business_flow_node_id")
    private Long businessFlowNodeId;

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getAuditingFlowId() {
        return AuditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        AuditingFlowId = auditingFlowId;
    }

    public Integer getCurrentAuditingStep() {
        return currentAuditingStep;
    }

    public void setCurrentAuditingStep(Integer currentAuditingStep) {
        this.currentAuditingStep = currentAuditingStep;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(Integer auditingStatus) {
        this.auditingStatus = auditingStatus;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getAuditingData() {
        return auditingData;
    }

    public void setAuditingData(String auditingData) {
        this.auditingData = auditingData;
    }

    public Long getBusinessFlowNodeId() {
        return businessFlowNodeId;
    }

    public void setBusinessFlowNodeId(Long businessFlowNodeId) {
        this.businessFlowNodeId = businessFlowNodeId;
    }
}
