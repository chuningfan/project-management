package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "pm_role_function_relation", indexes = {@Index(name = "pm_rfr_role_name", columnList = "role_name"),
        @Index(name = "pm_rfr_function_id", columnList = "function_id")})
public class RoleAndFunctionRelation extends BaseEntity {

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "function_id")
    private Long functionId;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

}
