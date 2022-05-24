package com.sxjkwm.pm.wxwork.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/23 11:23
 */
public class DepartmentDto implements Serializable {

    private Long id;

    private Long wxDeptId;

    private String name;

    private Long parentWxDeptId;

    private List<DepartmentDto> deptTree;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWxDeptId() {
        return wxDeptId;
    }

    public void setWxDeptId(Long wxDeptId) {
        this.wxDeptId = wxDeptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentWxDeptId() {
        return parentWxDeptId;
    }

    public void setParentWxDeptId(Long parentWxDeptId) {
        this.parentWxDeptId = parentWxDeptId;
    }

    public List<DepartmentDto> getDeptTree() {
        return deptTree;
    }

    public void setDeptTree(List<DepartmentDto> deptTree) {
        this.deptTree = deptTree;
    }
}
