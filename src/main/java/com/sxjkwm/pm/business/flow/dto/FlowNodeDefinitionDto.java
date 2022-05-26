package com.sxjkwm.pm.business.flow.dto;


import java.io.Serializable;

public class FlowNodeDefinitionDto implements Serializable {

    private Long id;

    private Long flowNodeId;

    private String propertyKey;

    private String propertyName;

    private Integer propertyIndex;

    private String propertyType;

    private String collectionPropertyHandler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(Integer propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCollectionPropertyHandler() {
        return collectionPropertyHandler;
    }

    public void setCollectionPropertyHandler(String collectionPropertyHandler) {
        this.collectionPropertyHandler = collectionPropertyHandler;
    }
}
