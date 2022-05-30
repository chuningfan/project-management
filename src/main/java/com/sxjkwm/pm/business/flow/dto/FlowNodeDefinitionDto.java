package com.sxjkwm.pm.business.flow.dto;


import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.constants.Constant;

import java.io.Serializable;
import java.util.List;

public class FlowNodeDefinitionDto implements Serializable {

    private Long id;

    private Long flowNodeId;

    private String propertyKey;

    private String propertyName;

    private Integer propertyIndex;

    private String propertyType;

    private Integer isDeleted = Constant.YesOrNo.NO.getValue();

    private String collectionPropertyHandler;

    private List<FlowNodeCollectionDefDto> flowNodeCollectionDefDtoList;

    private Integer nonNull;

    private List<FlowNodeSelectionDefinitionDto> selectionDtos;

    public FlowNodeDefinitionDto(FlowNodeDefinition definition) {
        this.id = definition.getId();
        this.flowNodeId = definition.getFlowNodeId();
        this.propertyKey = definition.getPropertyKey();
        this.propertyName = definition.getPropertyName();
        this.propertyIndex = definition.getPropertyIndex();
        this.propertyType = definition.getPropertyType();
        this.collectionPropertyHandler = definition.getCollectionPropertyHandler();
        this.isDeleted = definition.getIsDeleted();
        this.nonNull = definition.getNonNull();
    }

    public FlowNodeDefinitionDto() {
    }

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

    public List<FlowNodeCollectionDefDto> getFlowNodeCollectionDefDtoList() {
        return flowNodeCollectionDefDtoList;
    }

    public void setFlowNodeCollectionDefDtoList(List<FlowNodeCollectionDefDto> flowNodeCollectionDefDtoList) {
        this.flowNodeCollectionDefDtoList = flowNodeCollectionDefDtoList;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getNonNull() {
        return nonNull;
    }

    public void setNonNull(Integer nonNull) {
        this.nonNull = nonNull;
    }

    public List<FlowNodeSelectionDefinitionDto> getSelectionDtos() {
        return selectionDtos;
    }

    public void setSelectionDtos(List<FlowNodeSelectionDefinitionDto> selectionDtos) {
        this.selectionDtos = selectionDtos;
    }
}
