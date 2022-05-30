package com.sxjkwm.pm.business.flow.dto;

import com.sxjkwm.pm.business.flow.entity.FlowNodeSelectionDefinition;
import com.sxjkwm.pm.constants.Constant;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/5/30 14:41
 */
public class FlowNodeSelectionDefinitionDto implements Serializable {

    private Long id;

    private Long flowNodeId;

    private Integer selectionIndex;

    private Long refPropDefId;

    private String selectionName;

    private String selectionValue;

    private Integer isDeleted = Constant.YesOrNo.NO.getValue();

    private Boolean selected = Boolean.FALSE;

    public FlowNodeSelectionDefinitionDto() {
    }

    public FlowNodeSelectionDefinitionDto(FlowNodeSelectionDefinition definition) {
        this.id = definition.getId();
        this.flowNodeId = definition.getFlowNodeId();
        this.selectionIndex = definition.getSelectionIndex();
        this.refPropDefId = definition.getRefPropDefId();
        this.selectionName = definition.getSelectionName();
        this.selectionValue = definition.getSelectionValue();
        this.isDeleted = definition.getIsDeleted();
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
