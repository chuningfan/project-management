package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.common.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/5/25 7:26
 */
@Entity
@Table(name = "pm_flow_node_collection_definition", indexes = {@Index(name = "flow_nodeid", columnList = "flow_node_id"), @Index(name = "coll_propkey", columnList = "collection_property_def_id")})
public class FlowNodeCollectionDefinition extends BaseEntity {

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "collection_property_def_id")
    private Long collectionPropertyDefId;

    @Column(name = "header_key")
    private String headerKey;

    @Column(name = "header_name")
    private String headerName;

    @Column(name = "header_index")
    private Integer headerIndex;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "def_type")
    private String type;

    @Column(name = "non_null")
    private Integer nonNull;

    public FlowNodeCollectionDefinition() {
    }

    public FlowNodeCollectionDefinition(FlowNodeCollectionDefDto flowNodeCollectionDefDto) {
        this.id = flowNodeCollectionDefDto.getId();
        this.flowNodeId = flowNodeCollectionDefDto.getFlowNodeId();
        this.collectionPropertyDefId = flowNodeCollectionDefDto.getCollectionPropertyDefId();
        this.headerKey = flowNodeCollectionDefDto.getHeaderKey();
        this.headerName = flowNodeCollectionDefDto.getHeaderName();
        this.headerIndex = flowNodeCollectionDefDto.getHeaderIndex();
        this.propertyType = flowNodeCollectionDefDto.getPropertyType();
        this.type = flowNodeCollectionDefDto.getType();
        this.isDeleted = flowNodeCollectionDefDto.getIsDeleted();
        this.nonNull = flowNodeCollectionDefDto.getNonNull();
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

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNonNull() {
        return nonNull;
    }

    public void setNonNull(Integer nonNull) {
        this.nonNull = nonNull;
    }

    public boolean shouldBeModified(Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof FlowNodeCollectionDefinition)) {
            return false;
        }
        FlowNodeCollectionDefinition objInstance = (FlowNodeCollectionDefinition) obj;
        if (StringUtils.isBlank(propertyType)) {
            propertyType = "VARCHAR(255)";
        }
        if (StringUtils.isBlank(type)) {
            type = "string";
        }
        if (StringUtils.isBlank(objInstance.getPropertyType())) {
            objInstance.setPropertyType(propertyType);
        }
        if (StringUtils.isBlank(objInstance.getType())) {
            objInstance.setType(type);
        }
        return !headerKey.equals(objInstance.getHeaderKey()) || !propertyType.equals(objInstance.getPropertyType());
    }
}
