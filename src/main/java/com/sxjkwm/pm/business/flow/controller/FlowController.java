package com.sxjkwm.pm.business.flow.controller;


import com.sxjkwm.pm.business.flow.dto.FlowDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.service.FlowService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flow")
public class FlowController {

    private final FlowService flowService;

    @Autowired
    public FlowController(FlowService flowService) {
        this.flowService = flowService;
    }

    @PostMapping
    public RestResponse<Flow> create(@RequestBody Flow flow) {
        return RestResponse.get(flowService.createFlow(flow));
    }

    @PutMapping
    public RestResponse<Flow> update(@RequestBody FlowDto flowDto) {
        return RestResponse.get(flowService.updateFlow(flowDto));
    }

    @GetMapping("/{id}")
    public RestResponse<Flow> getOne(@PathVariable("id") Long id) {
        return RestResponse.get(flowService.getFlow(id));
    }

    @GetMapping
    public RestResponse<List<Flow>> getAll() {
        return RestResponse.get(flowService.getFlows(null));
    }

    @GetMapping("/list")
    public RestResponse<List<Flow>> getByName(@Param("flowName") String flowName) {
        return RestResponse.get(flowService.getFlows(flowName));
    }

}
