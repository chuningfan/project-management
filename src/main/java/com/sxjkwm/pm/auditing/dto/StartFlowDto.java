package com.sxjkwm.pm.auditing.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/6/27 9:16
 */
public class StartFlowDto implements Serializable {

    private Integer auditingDataType;

    private Long dataId;

    private Long auditingFlowId;

    private Long businessFlowNodeId;

    private Boolean onlySave;

    public Integer getAuditingDataType() {
        return auditingDataType;
    }

    public void setAuditingDataType(Integer auditingDataType) {
        this.auditingDataType = auditingDataType;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getAuditingFlowId() {
        return auditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        this.auditingFlowId = auditingFlowId;
    }

    public Long getBusinessFlowNodeId() {
        return businessFlowNodeId;
    }

    public void setBusinessFlowNodeId(Long businessFlowNodeId) {
        this.businessFlowNodeId = businessFlowNodeId;
    }

    public Boolean getOnlySave() {
        return onlySave;
    }

    public void setOnlySave(Boolean onlySave) {
        this.onlySave = onlySave;
    }
}
