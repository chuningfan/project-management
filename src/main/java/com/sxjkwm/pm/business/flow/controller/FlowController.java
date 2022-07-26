package com.sxjkwm.pm.business.flow.controller;


import com.sxjkwm.pm.business.flow.dto.FlowDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.service.FlowService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(Constant.API_FEATURE + "/flows")
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

    @GetMapping("/list")
    public RestResponse<List<Flow>> getByName(@Param("flowName") String flowName) {
        return RestResponse.of(flowService.getFlows(flowName));
    }

    @GetMapping
    public RestResponse<List<Flow>> getFlowList() {
        return RestResponse.of(flowService.getFlowList());
    }

    @DeleteMapping("/{id}")
    public RestResponse<Integer> removeFlow(@PathVariable("id") Long id) throws PmException {
        if (Objects.isNull(id)) {
            throw new PmException(PmError.ILLEGAL_PARAMETER);
        }
        int count = flowService.remove(id);
        if (count == 0) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        return RestResponse.of(count);
    }

}
