package com.sxjkwm.pm.business.material.entity;

import com.sxjkwm.pm.business.material.dto.CategoryDto;
import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:21
 */
@Entity
@Table(name = "pm_material_category")
public class Category extends BaseEntity {

    public Category() {
    }

    public Category(CategoryDto categoryDto) {
        this.id = categoryDto.getId();
        this.categoryName = categoryDto.getCategoryName();
    }

    @Column(name = "category_name")
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
