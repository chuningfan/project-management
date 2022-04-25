package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pm_role_function_relation")
public class RoleAndFunctionRelation extends BaseEntity {

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "function_id")
    private Long functionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

}
