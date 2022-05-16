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
@Table(name = "pm_project_node_property", indexes = {@Index(name = "projectnodeprop_projectInfo", columnList = "project_id,project_node_id,property_key")})
public class ProjectNodeProperty extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_node_id")
    private Long projectNodeId;

    @Column(name = "property_key")
    private String propertyKey;

    @Column(name = "property_value")
    private String propertyValue;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "property_index")
    private Integer propertyIndex;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public Integer getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(Integer propertyIndex) {
        this.propertyIndex = propertyIndex;
    }
}
