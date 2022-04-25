package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_department")
public class Department extends BaseEntity {

    @Column(name = "wx_dept_id")
    private Long wxDeptId;

    @Column(name = "dept_name")
    private String deptName;

    @Column(name = "dept_parent_id")
    private Long parentId;

    public Long getWxDeptId() {
        return wxDeptId;
    }

    public void setWxDeptId(Long wxDeptId) {
        this.wxDeptId = wxDeptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
