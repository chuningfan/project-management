package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

public class UserAndRoleRelation extends BaseEntity {

    private Long userId;

    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
