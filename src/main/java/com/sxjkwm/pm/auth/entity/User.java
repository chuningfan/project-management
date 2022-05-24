package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "pm_user", indexes = {@Index(name="pm_wx_user_id", columnList="wx_user_id")})
public class User extends BaseEntity {

    @Column(name = "wx_user_id")
    private String wxUserId;

    @Column(name = "user_name")
    private String name;

    @Column(name = "dept_ids")
    private String deptIds;

    @Column(name = "user_avatar")
    private String avatar;

    @Column(name = "open_user_id")
    private String openUserId;

    @Column(name = "mobile")
    private String mobile;

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOpenUserId() {
        return openUserId;
    }

    public void setOpenUserId(String openUserId) {
        this.openUserId = openUserId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
