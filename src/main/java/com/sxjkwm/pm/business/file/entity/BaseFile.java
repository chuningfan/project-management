package com.sxjkwm.pm.business.file.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseFile extends BaseEntity {

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "file_type")
    private String fileType;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
