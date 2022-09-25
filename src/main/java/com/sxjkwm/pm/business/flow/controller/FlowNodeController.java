package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.dto.NodeAndDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.service.FlowNodeService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(Constant.API_FEATURE + "/flowNodes")
public class FlowNodeController {

    private final FlowNodeService flowNodeService;

    @Autowired
    public FlowNodeController(FlowNodeService flowNodeService) {
        this.flowNodeService = flowNodeService;
    }

    @PostMapping("/{flowId}")
    public RestResponse<FlowNodeDto> createNodes(@PathVariable("flowId") Long flowId, @RequestBody FlowNodeDto flowNodeDto) {
        return RestResponse.of(flowNodeService.create(flowId, flowNodeDto));
    }

    @PutMapping("/{flowId}")
    public RestResponse<FlowNodeDto> updateNodes(@PathVariable("flowId") Long flowId, @RequestBody FlowNodeDto flowNodeDto) {
        return RestResponse.of(flowNodeService.update(flowId, flowNodeDto));
    }

    @GetMapping
    public RestResponse<List<FlowNode>> getNodes(@RequestParam("flowId") Long flowId) {
        return RestResponse.of(flowNodeService.getByFlowId(flowId));
    }

    @GetMapping(value = "/{flowId}")
    public RestResponse<List<FlowNode>> getFlowList(@PathVariable("flowId") Long flowId) {
        return RestResponse.of(flowNodeService.getFlowNodeList(flowId));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Integer> removeFlow(@PathVariable("id") Long id) throws PmException {
        if (Objects.isNull(id)) {
            throw new PmException(PmError.ILLEGAL_PARAMETER);
        }
        int count = flowNodeService.remove(id);
        if (count == 0) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        return RestResponse.of(count);
    }

    @PostMapping(value = "/sort")
    public RestResponse<Boolean> sort(@RequestBody List<Long> nodeIds) {
        return RestResponse.of(flowNodeService.sort(nodeIds));
    }

    @GetMapping("/nodeAndDefinitions")
    public RestResponse<List<NodeAndDefinitionDto>> fetchNodesAndDefByFlowId(@RequestParam("flowId") Long flowId) throws PmException {
        return RestResponse.of(flowNodeService.fetchNodeAndDefsByFlowId(flowId));
    }

}
