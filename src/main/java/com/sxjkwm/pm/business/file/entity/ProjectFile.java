package com.sxjkwm.pm.business.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "project_attachment")
public class ProjectFile extends BaseFile {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "object_id")
    private String objId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
