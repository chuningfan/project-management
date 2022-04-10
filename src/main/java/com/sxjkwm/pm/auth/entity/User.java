package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

public class User extends BaseEntity {

    private String name;

    private String deptName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
