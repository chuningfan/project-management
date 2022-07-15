package com.sxjkwm.pm.business.file.entity;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_pattern_file")
@Proxy(lazy = false)
public class PatternFile extends BaseFile {

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "object_name")
    private String objName;

    @Column(name = "file_category")
    private Integer fileCategory;

    @Column(name = "file_type")
    private Integer fileType;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public Integer getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(Integer fileCategory) {
        this.fileCategory = fileCategory;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
}
