package com.sxjkwm.pm.business.flow.controller;


import com.sxjkwm.pm.business.flow.dto.FlowDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.service.FlowService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/flows")
public class FlowController {

    private final FlowService flowService;

    @Autowired
    public FlowController(FlowService flowService) {
        this.flowService = flowService;
    }

    @PostMapping
    public RestResponse<Flow> create(@RequestBody Flow flow) {
        return RestResponse.of(flowService.createFlow(flow));
    }

    @PutMapping
    public RestResponse<Flow> update(@RequestBody FlowDto flowDto) {
        return RestResponse.of(flowService.updateFlow(flowDto));
    }

    @GetMapping("/{id}")
    public RestResponse<Flow> getOne(@PathVariable("id") Long id) {
        return RestResponse.of(flowService.getFlow(id));
    }

    @GetMapping
    public RestResponse<List<Flow>> getAll() {
        return RestResponse.of(flowService.getFlows(null));
    }

    @GetMapping("/list")
    public RestResponse<List<Flow>> getByName(@Param("flowName") String flowName) {
        return RestResponse.of(flowService.getFlows(flowName));
    }


    @PostMapping(value = "/getFlowList")
    public RestResponse<Page<Flow>> getFlowList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize,
                                                @RequestParam("flowName") String flowName) {
        if (Objects.isNull(pageSize)) {
            pageSize = 15;
        }
        return RestResponse.of(flowService.getFlowList(pageNum, pageSize, flowName));
    }

    @DeleteMapping("/{id}")
    public RestResponse<Object> removeFlow(@PathVariable("id") Long id) {
        try {
            if (Objects.isNull(id)) {
                return new RestResponse<>().setCode("500").setMessage("流程ID不能为空");
            }
            int count = flowService.remove(id);
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
