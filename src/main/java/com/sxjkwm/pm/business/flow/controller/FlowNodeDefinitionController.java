package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.flow.service.FlowNodeDefinitionService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/flowNodeDefinition")
public class FlowNodeDefinitionController {

    private final FlowNodeDefinitionService flowNodeDefinitionService;

    public FlowNodeDefinitionController(FlowNodeDefinitionService flowNodeDefinitionService) {
        this.flowNodeDefinitionService = flowNodeDefinitionService;
    }

    @PostMapping("/saveFlowNodeDefinition/{flowNodeId}")
    public RestResponse<List<FlowNodeDefinitionDto>> createDefinitions(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody List<FlowNodeDefinitionDto> flowNodeDefinitionDtos) {
        return RestResponse.of(flowNodeDefinitionService.create(flowNodeId, flowNodeDefinitionDtos));
    }
//
//    @PutMapping("/updateFlowNodeDefinition/{flowNodeId}")
//    public RestResponse<List<FlowNodeDto>> updateDefinitions(@PathVariable("flowNodeId") Long flowNodeId, @RequestBody List<FlowNodeDefinitionDto> flowNodeDefinitionDtos) {
//        return RestResponse.of(flowNodeDefinitionService.update(flowNodeId, flowNodeDefinitionDtos));
//    }




    @PostMapping(value = "/getFlowNodeDefinitionList")
    public RestResponse<Page<FlowNodeDefinition>> getFlowList(@RequestParam("pageNum") Integer pageNum,
                                                              @RequestParam("pageSize") Integer pageSize,
                                                              @RequestParam("flowNodeId") Long flowNodeId) {
        if (Objects.isNull(pageSize)) {
            pageSize = 15;
        }
        return RestResponse.of(flowNodeDefinitionService.getFlowNodeDefinitionList(pageNum, pageSize,flowNodeId));
    }
}
