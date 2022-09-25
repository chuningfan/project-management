package com.sxjkwm.pm.business.project.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/9/19 9:56
 */
public class AuditingFormRequest implements Serializable {

    private Long projectId; // 项目ID
    private Long flowId; // 流程ID
    private Long flowNodeId; // 审批流程节点ID
    private String formId; // 表单ID
    private String specialFormDataHandler; // 处理表单中特殊数据的handler
    private Integer command;  // 传1：保存为草稿，在企微-七巧云表单继续操作；传2：直接发起审批申请

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getSpecialFormDataHandler() {
        return specialFormDataHandler;
    }

    public void setSpecialFormDataHandler(String specialFormDataHandler) {
        this.specialFormDataHandler = specialFormDataHandler;
    }

    public Integer getCommand() {
        return command;
    }

    public void setCommand(Integer command) {
        this.command = command;
    }
}
