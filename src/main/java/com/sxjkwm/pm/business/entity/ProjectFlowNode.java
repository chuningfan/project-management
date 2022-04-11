package com.sxjkwm.pm.business.entity;

import com.sxjkwm.pm.business.dto.ProjectFlowNodeDto;
import com.sxjkwm.pm.common.BaseFlowNode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "pm_project_flow_node")
@Entity
public class ProjectFlowNode extends BaseFlowNode {

    @Column(name="pattern_files")
    private String patternFiles;

    @Column(name="description")
    private String description;

    public ProjectFlowNode(Long flowId, ProjectFlowNodeDto dto) {
        this.flowId = flowId;
        this.nodeVersion = dto.getNodeVersion();
        this.nodeName = dto.getNodeName();
        this.nodeIndex = dto.getNodeIndex();
        this.patternFiles = dto.getPatternFiles();
        this.description = dto.getDescription();
        this.skippable = dto.getSkippable();
        this.audittable = dto.getAudittable();
        this.formId = dto.getFormId();
    }

    public ProjectFlowNode() {
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
}
