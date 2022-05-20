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

    private Boolean hasValue = false;

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

    public Boolean getHasValue() {
        return hasValue;
    }

    public void setHasValue(Boolean hasValue) {
        this.hasValue = hasValue;
    }
}
