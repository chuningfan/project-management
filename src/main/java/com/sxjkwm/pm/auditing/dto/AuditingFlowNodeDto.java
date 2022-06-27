package com.sxjkwm.pm.auditing.dto;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:58
 */
public class AuditingFlowNodeDto {

    private Long id;

    private Long auditingFlowId;

    private Integer step;

    private Integer auditingType;

    private List<AuditorGroupDto> combineApproveGroups; // 当审批节点审批人为或签关系时，该集合应只有一个元素；当审批人为会签关系时，该集合有多个元素

    private Integer auditingStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuditingFlowId() {
        return auditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        this.auditingFlowId = auditingFlowId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getAuditingType() {
        return auditingType;
    }

    public void setAuditingType(Integer auditingType) {
        this.auditingType = auditingType;
    }

    public List<AuditorGroupDto> getCombineApproveGroups() {
        return combineApproveGroups;
    }

    public void setCombineApproveGroups(List<AuditorGroupDto> combineApproveGroups) {
        this.combineApproveGroups = combineApproveGroups;
    }

    public Integer getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(Integer auditingStatus) {
        this.auditingStatus = auditingStatus;
    }
}
