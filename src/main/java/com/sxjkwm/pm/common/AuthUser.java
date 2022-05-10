package com.sxjkwm.pm.common;

import java.io.Serializable;
import java.util.List;

public class AuthUser implements Serializable {

        private String userId;

        private String username;

        private List<Integer> departmentIds;

        private List<Long> roleIds;

        private String ipAddr;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<Integer> getDepartmentIds() {
            return departmentIds;
        }

        public void setDepartmentIds(List<Integer> departmentIds) {
            this.departmentIds = departmentIds;
        }

        public List<Long> getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(List<Long> roleIds) {
            this.roleIds = roleIds;
        }

        public String getIpAddr() {
            return ipAddr;
        }

        public void setIpAddr(String ipAddr) {
            this.ipAddr = ipAddr;
        }
    }