package com.sxjkwm.pm.business.project.entity;

import com.sxjkwm.pm.common.BaseEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "pm_project",
        indexes = {@Index(name="project_flowId", columnList = "flow_id"),
                @Index(name="project_unionIndex", columnList = "owner_user_id,project_code,project_code,project_status"),
                @Index(name="project_requireid", columnList = "require_part_id")})
@Entity
@Proxy(lazy = false)
public class Project extends BaseEntity {

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "require_part_id")
    private Long requirePartId;

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

    @Column(name = "project_time")
    private Long projectTime;

    @Column(name = "goods_receive_time")
    private Long goodsReceiveTime;

    @Column(name = "goods_receive_address")
    private String goodsReceiveAddress;

    @Column(name = "goods_receiver")
    private String goodsReceiver;

    @Column(name = "goods_receiver_mobile")
    private String goodsReceiverMobile;

    @Column(name = "supply_period")
    private String supplyPeriod;

    @Column(name = "purchase_scope")
    private String purchaseScope;

    @Column(name = "deadline")
    private Long deadLine;

    @Column(name = "projectType")
    private Integer projectType;

    @Column(name = "payment_type", length = 4000)
    private String paymentType;

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

    public Long getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(Long projectTime) {
        this.projectTime = projectTime;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
