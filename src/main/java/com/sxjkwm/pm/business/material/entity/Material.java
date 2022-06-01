package com.sxjkwm.pm.business.material.entity;

import com.sxjkwm.pm.business.material.dto.MaterialDto;
import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:23
 */
@Entity
@Table(name = "pm_material")
public class Material extends BaseEntity {

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "material_name")
    private String materialName;

    public Material() {
    }

    public Material(MaterialDto materialDto) {
        this.id = materialDto.getId();
        this.parentId = materialDto.getParentId();
        this.materialName = materialDto.getMaterialName();
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
