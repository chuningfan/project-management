package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Service
public class ContextFactoryImpl implements ContextFactory<ContextFactoryImpl.AuthUser> {
    private static final ThreadLocal<Context<ContextFactoryImpl.AuthUser>> contextThreadLocal = new ThreadLocal<>();
    @Override
    public Context<ContextFactoryImpl.AuthUser> get() {
        Context<ContextFactoryImpl.AuthUser> context = contextThreadLocal.get();
        if (Objects.isNull(context)) {
            context = new ContextImpl();
            contextThreadLocal.set(context);
        }
        return context;
    }

    @Override
    public void remove() {
        contextThreadLocal.remove();
    }

    public static class AuthUser implements Serializable {

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

}
