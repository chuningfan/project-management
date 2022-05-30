package com.sxjkwm.pm.business.flow.entity;

import com.sxjkwm.pm.business.flow.dto.FlowNodeSelectionDefinitionDto;
import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/5/30 14:36
 */

@Entity
@Table(name = "pm_selection_definition")
public class FlowNodeSelectionDefinition extends BaseEntity {

    @Column(name = "flow_node_id")
    private Long flowNodeId;

    @Column(name = "selection_index")
    private Integer selectionIndex;

    @Column(name = "ref_prop_def_id")
    private Long refPropDefId;

    @Column(name = "selection_name")
    private String selectionName;

    @Column(name = "selection_value")
    private String selectionValue;

    public FlowNodeSelectionDefinition() {
    }

    public FlowNodeSelectionDefinition(FlowNodeSelectionDefinitionDto flowNodeSelectionDto) {
        this.id = flowNodeSelectionDto.getId();
        this.flowNodeId = flowNodeSelectionDto.getFlowNodeId();
        this.isDeleted = flowNodeSelectionDto.getIsDeleted();
        this.selectionIndex = flowNodeSelectionDto.getSelectionIndex();
        this.selectionName = flowNodeSelectionDto.getSelectionName();
        this.selectionValue = flowNodeSelectionDto.getSelectionValue();
        this.refPropDefId = flowNodeSelectionDto.getRefPropDefId();
    }

    public Integer getSelectionIndex() {
        return selectionIndex;
    }

    public void setSelectionIndex(Integer selectionIndex) {
        this.selectionIndex = selectionIndex;
    }

    public Long getRefPropDefId() {
        return refPropDefId;
    }

    public void setRefPropDefId(Long refPropDefId) {
        this.refPropDefId = refPropDefId;
    }

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public String getSelectionValue() {
        return selectionValue;
    }

    public void setSelectionValue(String selectionValue) {
        this.selectionValue = selectionValue;
    }

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }
}
