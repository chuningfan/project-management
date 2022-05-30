package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

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
}
