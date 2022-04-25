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

    @Column(name = "node_index")
    protected Integer nodeIndex;

    // 集成道一云审批流
    @Column(name = "form_id")
    protected String formId;

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

    public Integer getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(Integer nodeIndex) {
        this.nodeIndex = nodeIndex;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

}
