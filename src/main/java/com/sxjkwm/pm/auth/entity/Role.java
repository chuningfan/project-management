package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;

import java.util.Set;

public class Role extends BaseEntity {

    private String name;

    private Set<String> accessibleUris;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAccessibleUris() {
        return accessibleUris;
    }

    public void setAccessibleUris(Set<String> accessibleUris) {
        this.accessibleUris = accessibleUris;
    }
}
