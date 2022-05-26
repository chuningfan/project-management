package com.sxjkwm.pm.business.flow.controller;

import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.service.FlowNodeService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/projectFlowNode")
public class FlowNodeController {

    private final FlowNodeService flowNodeService;

    @Autowired
    public FlowNodeController(FlowNodeService flowNodeService) {
        this.flowNodeService = flowNodeService;
    }

    @PostMapping("/saveFlowNode/{flowId}")
    public RestResponse<List<FlowNodeDto>> createNodes(@PathVariable("flowId") Long flowId, @RequestBody List<FlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.of(flowNodeService.create(flowId, projectFlowNodeDtos));
    }

    @PutMapping("/updateFlowNode/{flowId}")
    public RestResponse<List<FlowNodeDto>> updateNodes(@PathVariable("flowId") Long flowId, @RequestBody List<FlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.of(flowNodeService.update(flowId, projectFlowNodeDtos));
    }

    @GetMapping
    public RestResponse<List<FlowNode>> getNodes(@RequestParam("flowId") Long flowId) {
        FlowNode flowNode = new FlowNode();
        flowNode.setFlowId(flowId);
        flowNode.setSkippable(null);
        return RestResponse.of(flowNodeService.getByConditions(flowNode, Sort.by(Sort.Direction.fromString("ASC"), "nodeIndex")));
    }

    @PostMapping(value = "/getFlowNodeList")
    public RestResponse<List<FlowNode>> getFlowList(@RequestParam("flowId") Long flowId) {

        return RestResponse.of(flowNodeService.getFlowNodeList(flowId));
    }

    @PostMapping(value = "/deleteNode")
    public RestResponse<Object> removeFlow(@RequestBody Flow flow) {
        try {
            Long id = flow.getId();
            if (Objects.isNull(id)) {
                return new RestResponse<>().setCode("500").setMessage("节点ID不能为空");
            }
            int count = flowNodeService.remove(id);
            if (count > 0) {
                return new RestResponse<>().setCode("200").setMessage("删除成功");
            } else {
                return new RestResponse<>().setCode("500").setMessage("删除失败");
            }
        } catch (Exception e) {
            return new RestResponse<>().setCode("500").setMessage("删除失败");
        }
    }

}
