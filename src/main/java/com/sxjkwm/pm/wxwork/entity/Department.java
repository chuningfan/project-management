package com.sxjkwm.pm.wxwork.entity;

import com.sxjkwm.pm.common.AuditableEntity;

import javax.persistence.*;

@Entity
@Table(name = "department")
public class Department extends AuditableEntity {

    @Id
    private Long deptId;

    @Column(name = "dept_name")
    private String deptName;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
