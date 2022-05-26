package com.sxjkwm.pm.business.project.dto;

import com.sxjkwm.pm.common.BaseCollectionProperty;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:39
 */
public class ProjectNodePropertyDto implements Serializable {

    private Long id;

    private Long projectId;

    private Long projectNodeId;

    private String propertyKey;

    private String propertyValue;

    private String propertyType;

    private Integer propertyIndex;

    private String propertyName;

    private String fileName;

    private List<Map<String, Object>> collectionData;

    private String collectionPropertyHandler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public Integer getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(Integer propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public List<Map<String, Object>> getCollectionData() {
        return collectionData;
    }

    public void setCollectionData(List<Map<String, Object>> collectionData) {
        this.collectionData = collectionData;
    }

    public String getCollectionPropertyHandler() {
        return collectionPropertyHandler;
    }

    public void setCollectionPropertyHandler(String collectionPropertyHandler) {
        this.collectionPropertyHandler = collectionPropertyHandler;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
