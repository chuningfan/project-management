package com.sxjkwm.pm.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Vic.Chu
 * @date 2022/5/15 15:04
 */
@MappedSuperclass
public class BaseCollectionProperty extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_node_id")
    private Long projectNodeId;

    @Column(name = "project_node_property_key")
    private String projectNodePropertyKey;

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

    public String getProjectNodePropertyKey() {
        return projectNodePropertyKey;
    }

    public void setProjectNodePropertyKey(String projectNodePropertyKey) {
        this.projectNodePropertyKey = projectNodePropertyKey;
    }
}
