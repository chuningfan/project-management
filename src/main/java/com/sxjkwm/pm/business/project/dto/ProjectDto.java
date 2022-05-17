package com.sxjkwm.pm.business.project.dto;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;

public class ProjectDto implements Serializable {

    private Long id;

    private String projectName;

    private Long flowId;

    private String requirePart; // 业主

    private String supplyPart; // 供应商

    private Long currentNodeId;

    private String description;

    private String deptName;

    private String ownerUserId;

    private BigDecimal budget;

    private String projectCode;  // 项目编号

    private Integer projectStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getRequirePart() {
        return requirePart;
    }

    public void setRequirePart(String requirePart) {
        this.requirePart = requirePart;
    }

    public String getSupplyPart() {
        return supplyPart;
    }

    public void setSupplyPart(String supplyPart) {
        this.supplyPart = supplyPart;
    }

    public Long getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Long currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }

}
