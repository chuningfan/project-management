package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.service.FlowNodeService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-flow-node")
public class FlowNodeController {

    private final FlowNodeService flowNodeService;

    @Autowired
    public FlowNodeController(FlowNodeService flowNodeService) {
        this.flowNodeService = flowNodeService;
    }

    @PostMapping("/{flowId}")
    public RestResponse<List<FlowNodeDto>> createNodes(@PathVariable("flowId") Long flowId, @RequestBody List<FlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.get(flowNodeService.create(flowId, projectFlowNodeDtos));
    }

    @PutMapping("/{flowId}")
    public RestResponse<List<FlowNodeDto>> updateNodes(@PathVariable("flowId") Long flowId, @RequestBody List<FlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.get(flowNodeService.update(flowId, projectFlowNodeDtos));
    }

    @GetMapping("/flowId")
    public RestResponse<List<FlowNode>> getNodes(@PathVariable("flowId") Long flowId) {
        return RestResponse.get(flowNodeService.getNodes(flowId));
    }

}
