package com.sxjkwm.pm.business.file.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Proxy(lazy = false)
@Table(name = "pm_project_attachment", indexes = {@Index(name = "pm_attach_union_indexes", columnList = "project_id,flow_node_id,property_def_id")})
public class ProjectFile extends BaseFile {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "object_name")
    private String objectName;

    @Column(name = "bucket_name")
    private String bucketName;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

}
