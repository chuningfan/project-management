package com.sxjkwm.pm.business.project.entity;

import com.sxjkwm.pm.common.BaseEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "pm_project", indexes = {@Index(name="project_flowId", columnList = "flow_id"), @Index(name="project_unionIndex", columnList = "owner_user_id,project_code,project_code,project_status")})
@Entity
@Proxy(lazy = false)
public class Project extends BaseEntity {

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "require_part")
    private String requirePart; // 业主

    @Column(name = "supply_part")
    private String supplyPart; // 供应商

    @Column(name = "current_node_id")
    private Long currentNodeId;

    @Column(name = "project_description", length=4000)
    private String description;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "owner_user_id")
    private String ownerUserId;

    @Column(name = "budget")
    private BigDecimal budget;

    @Column(name = "project_code")
    private String projectCode;  // 项目编号

    @Column(name = "project_status")
    private Integer projectStatus;

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
