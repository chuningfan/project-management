package com.sxjkwm.pm.business.project.entity;

import com.sxjkwm.pm.common.BaseEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_project_node")
@Entity
@Proxy(lazy = false)
public class ProjectNode extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "node_status")
    private Integer nodeStatus;

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
}
