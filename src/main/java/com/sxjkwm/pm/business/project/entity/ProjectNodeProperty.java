package com.sxjkwm.pm.business.project.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/5/15 12:43
 */
@Entity
@Table(name = "pm_project_node_property", indexes = {@Index(name = "projectnodeprop_projectInfo", columnList = "project_id,project_node_id,flow_node_property_def_id")})
public class ProjectNodeProperty extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_node_id")
    private Long projectNodeId;

    @Column(name = "flow_node_property_def_id")
    private Long flowNodePropertyDefId;

    @Column(name = "property_value")
    private String propertyValue;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public Long getFlowNodePropertyDefId() {
        return flowNodePropertyDefId;
    }

    public void setFlowNodePropertyDefId(Long flowNodePropertyDefId) {
        this.flowNodePropertyDefId = flowNodePropertyDefId;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
