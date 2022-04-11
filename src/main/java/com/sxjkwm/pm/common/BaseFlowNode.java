package com.sxjkwm.pm.common;

import com.sxjkwm.pm.constants.Constant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseFlowNode extends BaseEntity {

    @Column(name = "flow_id")
    protected Long flowId;

    @Column(name = "node_name")
    protected String nodeName;

    @Column(name = "skippable")
    protected Integer skippable = Constant.YesOrNo.NO.getValue();

    @Column(name = "audittable")
    protected Integer audittable = Constant.YesOrNo.NO.getValue();

    @Column(name = "node_index")
    protected Integer nodeIndex;

    @Column(name = "node_version")
    protected Integer nodeVersion;

    // 集成道一云审批流
    @Column(name = "form_id")
    protected String formId;

    @Column(name= "include_progress_percentage")
    protected Integer includeProgressPercentage;

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
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

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getIncludeProgressPercentage() {
        return includeProgressPercentage;
    }

    public void setIncludeProgressPercentage(Integer includeProgressPercentage) {
        this.includeProgressPercentage = includeProgressPercentage;
    }
}
