package com.sxjkwm.pm.business.file.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "project_name")
public class ProjectFile extends BaseFile {

    @Column(name = "object_id")
    private String objId;

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}
