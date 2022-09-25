package com.sxjkwm.pm.common;

import com.sxjkwm.pm.constants.Constant;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseFlowNode extends BaseEntity {

    @Column(name = "flow_id")
    protected Long flowId;

    @Column(name = "node_name")
    protected String nodeName;

    @Column(name = "skippable", columnDefinition = " INT(1) DEFAULT 1")
    protected Integer skippable;

    @Column(name = "node_index")
    protected Integer nodeIndex;

    @Column(name = "need_to_audit")
    protected Integer needAudit;

    @Column(name = "auditing_flow_id")
    protected Long auditingFlowId;

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

    public Integer getNeedAudit() {
        return needAudit;
    }

    public void setNeedAudit(Integer needAudit) {
        this.needAudit = needAudit;
    }

    public Long getAuditingFlowId() {
        return auditingFlowId;
    }

    public void setAuditingFlowId(Long auditingFlowId) {
        this.auditingFlowId = auditingFlowId;
    }

}
