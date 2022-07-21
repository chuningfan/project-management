package com.sxjkwm.pm.business.file.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/7/18 15:24
 */
public class PatternFileDto implements Serializable {

    private Long id;

    private String fileName;

    private Integer category;

    private String categoryName;

    private Integer fileType;

    private String typeName;

    public PatternFileDto(Long id, String fileName, Integer category, String categoryName, Integer fileType, String typeName) {
        this.id = id;
        this.fileName = fileName;
        this.category = category;
        this.categoryName = categoryName;
        this.fileType = fileType;
        this.typeName = typeName;
    }

    public PatternFileDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
