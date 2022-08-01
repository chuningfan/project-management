package com.sxjkwm.pm.business.project.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProjectDto implements Serializable {

    private Long id;

    private String projectName;

    private Long flowId;

    private Long requirePartId;

    private String requirePart; // 业主

    private Long currentNodeId;

    private String description;

    private String deptName;

    private String ownerUserId;

    private BigDecimal budget;

    private String projectCode;  // 项目编号

    private Integer projectStatus = 0;

    private Long projectTime;

    private Long goodsReceiveTime;

    private String goodsReceiveAddress;

    private String goodsReceiver;

    private String goodsReceiverMobile;

    private String supplyPeriod;

    private String purchaseScope;

    private Integer isDeleted = 0;

    private Long deadLine;

    private Integer projectType;

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

    public Long getRequirePartId() {
        return requirePartId;
    }

    public void setRequirePartId(Long requirePartId) {
        this.requirePartId = requirePartId;
    }

    public String getRequirePart() {
        return requirePart;
    }

    public void setRequirePart(String requirePart) {
        this.requirePart = requirePart;
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

    public Long getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(Long projectTime) {
        this.projectTime = projectTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Long getGoodsReceiveTime() {
        return goodsReceiveTime;
    }

    public void setGoodsReceiveTime(Long goodsReceiveTime) {
        this.goodsReceiveTime = goodsReceiveTime;
    }

    public String getGoodsReceiveAddress() {
        return goodsReceiveAddress;
    }

    public void setGoodsReceiveAddress(String goodsReceiveAddress) {
        this.goodsReceiveAddress = goodsReceiveAddress;
    }

    public String getGoodsReceiver() {
        return goodsReceiver;
    }

    public void setGoodsReceiver(String goodsReceiver) {
        this.goodsReceiver = goodsReceiver;
    }

    public String getGoodsReceiverMobile() {
        return goodsReceiverMobile;
    }

    public void setGoodsReceiverMobile(String goodsReceiverMobile) {
        this.goodsReceiverMobile = goodsReceiverMobile;
    }

    public String getSupplyPeriod() {
        return supplyPeriod;
    }

    public void setSupplyPeriod(String supplyPeriod) {
        this.supplyPeriod = supplyPeriod;
    }

    public String getPurchaseScope() {
        return purchaseScope;
    }

    public void setPurchaseScope(String purchaseScope) {
        this.purchaseScope = purchaseScope;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }
}
