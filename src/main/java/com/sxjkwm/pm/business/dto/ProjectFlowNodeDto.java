package com.sxjkwm.pm.business.dto;

import java.io.Serializable;
import java.util.List;

public class ProjectFlowNodeDto implements Serializable {

    private Long id;

    private String nodeName;

    private Integer isDeleted;

    private Integer skippable;

    private Integer audittable;

    private Integer nodeIndex;

    private Integer nodeVersion;

    private String patternFiles;

    private String description;

    // 道一云审批表单
    private String formId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSkippable() {
        return skippable;
    }

    public void setSkippable(Integer skippable) {
        this.skippable = skippable;
    }

    public Integer getAudittable() {
        return audittable;
    }

    public void setAudittable(Integer audittable) {
        this.audittable = audittable;
    }

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public Integer getNodeVersion() {
        return nodeVersion;
    }

    public void setNodeVersion(Integer nodeVersion) {
        this.nodeVersion = nodeVersion;
    }

    public String getPatternFiles() {
        return patternFiles;
    }

    public void setPatternFiles(String patternFiles) {
        this.patternFiles = patternFiles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
