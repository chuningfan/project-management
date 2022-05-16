package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.service.FlowNodeService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projectFlowNode")
public class FlowNodeController {

    private final FlowNodeService flowNodeService;

    @Autowired
    public FlowNodeController(FlowNodeService flowNodeService) {
        this.flowNodeService = flowNodeService;
    }

    @PostMapping("/{flowId}")
    public RestResponse<List<FlowNodeDto>> createNodes(@PathVariable("flowId") Long flowId, @RequestBody List<FlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.of(flowNodeService.create(flowId, projectFlowNodeDtos));
    }

    @PutMapping("/{flowId}")
    public RestResponse<List<FlowNodeDto>> updateNodes(@PathVariable("flowId") Long flowId, @RequestBody List<FlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.of(flowNodeService.update(flowId, projectFlowNodeDtos));
    }

    @GetMapping
    public RestResponse<List<FlowNode>> getNodes(@RequestParam("flowId") Long flowId) {
        FlowNode flowNode = new FlowNode();
        flowNode.setFlowId(flowId);
        return RestResponse.of(flowNodeService.getByConditions(flowNode, Sort.by(Sort.Direction.fromString("ASC"), "nodeIndex")));
    }

}
