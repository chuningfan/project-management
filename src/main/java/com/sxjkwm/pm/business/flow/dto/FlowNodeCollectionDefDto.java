package com.sxjkwm.pm.business.flow.dto;

import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.constants.Constant;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/5/25 7:37
 */
public class FlowNodeCollectionDefDto implements Serializable {

    private Long id;

    private Long flowNodeId;

    private Long collectionPropertyDefId;

    private String headerKey;

    private String headerName;

    private Integer headerIndex;

    private String propertyType;

    private Integer isDeleted = Constant.YesOrNo.NO.getValue();

    public FlowNodeCollectionDefDto(FlowNodeCollectionDefinition definition) {
        this.id = definition.getId();
        this.flowNodeId = definition.getFlowNodeId();
        this.collectionPropertyDefId = definition.getCollectionPropertyDefId();
        this.headerKey = definition.getHeaderKey();
        this.headerName = definition.getHeaderName();
        this.headerIndex = definition.getHeaderIndex();
        this.isDeleted = definition.getIsDeleted();
        this.propertyType = definition.getPropertyType();
    }

    public FlowNodeCollectionDefDto() {
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

    public Long getCollectionPropertyDefId() {
        return collectionPropertyDefId;
    }

    public void setCollectionPropertyDefId(Long collectionPropertyDefId) {
        this.collectionPropertyDefId = collectionPropertyDefId;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public Integer getHeaderIndex() {
        return headerIndex;
    }

    public void setHeaderIndex(Integer headerIndex) {
        this.headerIndex = headerIndex;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

}
