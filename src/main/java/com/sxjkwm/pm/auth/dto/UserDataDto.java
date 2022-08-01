package com.sxjkwm.pm.auth.dto;

import com.sxjkwm.pm.function.dto.FunctionDto;

import java.io.Serializable;
import java.util.List;

public class UserDataDto implements Serializable {

    private Long userId;

    private String wxUserId;

    private String username;

    private List<String> deptNames;

    private List<String> roleNames;

    private List<FunctionDto> accessibleFunctions;

    private String avatar;

    private String ipAddr;

    private String mobile;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(String wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(List<String> deptNames) {
        this.deptNames = deptNames;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<FunctionDto> getAccessibleFunctions() {
        return accessibleFunctions;
    }

    public void setAccessibleFunctions(List<FunctionDto> accessibleFunctions) {
        this.accessibleFunctions = accessibleFunctions;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
