package com.sxjkwm.pm.business.material.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:34
 */
public class MaterialDto implements Serializable {

    private Long id;

    private Long parentId;

    private String materialName;

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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
