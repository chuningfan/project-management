package com.sxjkwm.pm.business.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_project_attachment")
public class ProjectFile extends BaseFile {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "object_name")
    private String objName;

    @Column(name = "project_node_id")
    private String projectNodeId;

    @Column(name = "file_type")
    private String fileType;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(String projectNodeId) {
        this.projectNodeId = projectNodeId;
    }
}
