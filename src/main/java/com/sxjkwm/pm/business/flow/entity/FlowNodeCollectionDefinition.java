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
@Table(name = "pm_flow_node_collection_definition", indexes = {@Index(name = "flow_nodeid", columnList = "flow_node_id"), @Index(name = "coll_propkey", columnList = "collection_prop_key")})
public class FlowNodeCollectionDefinition extends BaseEntity {

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "collection_prop_key")
    private String collectionPropKey;

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

    public String getCollectionPropKey() {
        return collectionPropKey;
    }

    public void setCollectionPropKey(String collectionPropKey) {
        this.collectionPropKey = collectionPropKey;
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
