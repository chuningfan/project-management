package com.sxjkwm.pm.business.project.dto;

import com.sxjkwm.pm.constants.Constant;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:38
 */
public class ProjectNodeDto implements Serializable {

    private Long id;

    private Long projectId;

    private Long flowNodeId;

    private Integer nodeStatus = Constant.ProjectNodeStatus.INPROGRESS.getValue();

    private List<ProjectNodePropertyDto> propertyDtos;

    private Integer projectStatus = 0;

    private Integer auditingStatus;

    private Boolean lastNode = false;

    private String formId;

    private String specialFormDataHandler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public Integer getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(Integer nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public List<ProjectNodePropertyDto> getPropertyDtos() {
        return propertyDtos;
    }

    public void setPropertyDtos(List<ProjectNodePropertyDto> propertyDtos) {
        this.propertyDtos = propertyDtos;
    }

    public Integer getAuditingStatus() {
        return auditingStatus;
    }

    public void setAuditingStatus(Integer auditingStatus) {
        this.auditingStatus = auditingStatus;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Boolean getLastNode() {
        return lastNode;
    }

    public void setLastNode(Boolean lastNode) {
        this.lastNode = lastNode;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getSpecialFormDataHandler() {
        return specialFormDataHandler;
    }

    public void setSpecialFormDataHandler(String specialFormDataHandler) {
        this.specialFormDataHandler = specialFormDataHandler;
    }
}
