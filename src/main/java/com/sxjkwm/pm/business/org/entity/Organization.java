package com.sxjkwm.pm.business.org.entity;

import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

/**
 * @author Vic.Chu
 * @date 2022/5/24 18:16
 */
@Entity
@Table(name = "pm_organization", indexes = {@Index(name = "org_pid", columnList = "parent_id")})
public class Organization {

    @Id
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "org_level")
    private Integer orgLevel;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "full_path")
    private String fullPath;

    @Column(name = "full_name")
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}


