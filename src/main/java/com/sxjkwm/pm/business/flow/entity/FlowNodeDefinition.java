package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/5/15 12:27
 */
@Entity
@Table(name = "pm_flow_node_definition")
public class FlowNodeDefinition extends BaseEntity {

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "property_key")
    private String propertyKey;

    @Column(name = "property_name")
    private String propertyName;

    @Column(name = "property_index")
    private Integer propertyIndex;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "collection_property_handler")
    private String collectionPropertyHandler;

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
