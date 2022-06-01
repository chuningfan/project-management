package com.sxjkwm.pm.business.material.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:28
 */
public class CategoryDto implements Serializable {

    private Long id;

    private String categoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
