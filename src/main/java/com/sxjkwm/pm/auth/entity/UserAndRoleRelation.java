package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "pm_user_role_relation", indexes = {@Index(name="pm_urr_user_id", columnList="wx_user_id"), @Index(name = "pm_urr_role_id", columnList = "role_id")})
public class UserAndRoleRelation extends BaseEntity {

    @Column(name="wx_user_id")
    private String wxUserId;

    @Column(name="role_id")
    private Long roleId;

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
