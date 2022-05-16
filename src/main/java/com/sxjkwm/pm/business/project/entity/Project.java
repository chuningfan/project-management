package com.sxjkwm.pm.business.project.entity;

import com.sxjkwm.pm.common.BaseEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "pm_project")
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

    @Column(name = "project_description")
    private String description;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "owner_user_id")
    private String ownerUserId;

    @Column(name = "budget")
    private BigDecimal budget;

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
}
