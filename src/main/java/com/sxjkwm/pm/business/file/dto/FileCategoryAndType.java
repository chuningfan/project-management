package com.sxjkwm.pm.business.file.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/7/13 16:23
 */
public class FileCategoryAndType implements Serializable {

    private Integer categoryId;

    private String categoryName;

    private Integer fileTypeId;

    private String fileTypeName;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(Integer fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(String fileTypeName) {
        this.fileTypeName = fileTypeName;
    }
}
