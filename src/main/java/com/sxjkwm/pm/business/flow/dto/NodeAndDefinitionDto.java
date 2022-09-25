package com.sxjkwm.pm.business.flow.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/9/25 15:29
 */
public class NodeAndDefinitionDto implements Serializable {

    private Long flowNodeId;

    private String flowNodeName;

    private List<FlowNodeDefinitionDto> definitionDtoList;

    public Long getFlowNodeId() {
        return flowNodeId;
    }

    public void setFlowNodeId(Long flowNodeId) {
        this.flowNodeId = flowNodeId;
    }

    public String getFlowNodeName() {
        return flowNodeName;
    }

    public void setFlowNodeName(String flowNodeName) {
        this.flowNodeName = flowNodeName;
    }

    public List<FlowNodeDefinitionDto> getDefinitionDtoList() {
        return definitionDtoList;
    }

    public void setDefinitionDtoList(List<FlowNodeDefinitionDto> definitionDtoList) {
        this.definitionDtoList = definitionDtoList;
    }
}
