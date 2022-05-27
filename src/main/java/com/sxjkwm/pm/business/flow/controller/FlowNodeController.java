package com.sxjkwm.pm.business.flow.controller;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.dto.NodeIndexDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.service.FlowNodeService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/flowNodes")
public class FlowNodeController {

    private final FlowNodeService flowNodeService;

    @Autowired
    public FlowNodeController(FlowNodeService flowNodeService) {
        this.flowNodeService = flowNodeService;
    }

    @PostMapping("/{flowId}")
    public RestResponse<List<FlowNodeDto>> createNodes(@PathVariable("flowId") Long flowId, @RequestBody FlowNodeDto flowNodeDto) {
        List<FlowNodeDto> flowNodeDtos = Lists.newArrayList();
        flowNodeDtos.add(flowNodeDto);
        return RestResponse.of(flowNodeService.create(flowId, flowNodeDtos));
    }

    @PutMapping("/{flowId}")
    public RestResponse<List<FlowNodeDto>> updateNodes(@PathVariable("flowId") Long flowId, @RequestBody FlowNodeDto flowNodeDto) {
        return RestResponse.of(flowNodeService.update(flowId, flowNodeDto));
    }

    @GetMapping
    public RestResponse<List<FlowNode>> getNodes(@RequestParam("flowId") Long flowId) {
        FlowNode flowNode = new FlowNode();
        flowNode.setFlowId(flowId);
        flowNode.setSkippable(null);
        return RestResponse.of(flowNodeService.getByConditions(flowNode, Sort.by(Sort.Direction.fromString("ASC"), "nodeIndex")));
    }

    @GetMapping(value = "/{flowId}")
    public RestResponse<List<FlowNode>> getFlowList(@PathVariable("flowId") Long flowId) {
        return RestResponse.of(flowNodeService.getFlowNodeList(flowId));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Object> removeFlow(@PathVariable("id") Long id) {
        try {
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

    @PostMapping(value = "/sort")
    public RestResponse<List<FlowNode>> sort(@RequestBody List<NodeIndexDto> nodeIndexDtos) {
        return RestResponse.of(flowNodeService.sort(nodeIndexDtos));
    }

}
