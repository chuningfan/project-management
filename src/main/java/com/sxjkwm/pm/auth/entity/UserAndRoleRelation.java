package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "pm_user_role_relation", indexes = {@Index(name="pm_urr_user_id", columnList="wx_user_id"), @Index(name = "pm_urr_role_name", columnList = "role_name")})
public class UserAndRoleRelation extends BaseEntity {

    @Column(name="wx_user_id")
    private String wxUserId;

    @Column(name = "role_name")
    private String roleName;

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
