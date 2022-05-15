package com.sxjkwm.pm.business.project.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_project_node")
@Entity
public class ProjectNode extends BaseEntity {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "attached_files")
    private String attachedFiles;

    @Column(name = "is_done")
    private Integer isDone;

    @Column(name = "project_node_name")
    private String projectNodeName;

    @Column(name = "node_time")
    private Long nodeTime;

    @Column(name = "audit_status")
    private Integer auditStatus;

    @Column(name = "description")
    private String description;

    @Column(name = "node_version")
    private Integer nodeVersion;

    @Column(name = "form_id")
    protected String formatId;

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

    public String getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(String attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Integer getIsDone() {
        return isDone;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public Long getNodeTime() {
        return nodeTime;
    }

    public void setNodeTime(Long nodeTime) {
        this.nodeTime = nodeTime;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getProjectNodeName() {
        return projectNodeName;
    }

    public void setProjectNodeName(String projectNodeName) {
        this.projectNodeName = projectNodeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(Integer nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

}
