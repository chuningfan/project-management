package com.sxjkwm.pm.business.controller;


import com.sxjkwm.pm.business.entity.ProjectFlow;
import com.sxjkwm.pm.business.service.ProjectFlowService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-flow")
public class ProjectFlowController {

    private final ProjectFlowService projectFlowService;

    @Autowired
    public ProjectFlowController(ProjectFlowService projectFlowService) {
        this.projectFlowService = projectFlowService;
    }

    @PostMapping
    public RestResponse<ProjectFlow> create(@RequestBody ProjectFlow projectFlow) {
        return RestResponse.get(projectFlowService.createFlow(projectFlow));
    }

    @PutMapping
    public RestResponse<ProjectFlow> update(@RequestBody ProjectFlow projectFlow) {
        return RestResponse.get(projectFlowService.updateFlow(projectFlow));
    }

    @GetMapping("/{id}")
    public RestResponse<ProjectFlow> getOne(@PathVariable("id") Long id) {
        return RestResponse.get(projectFlowService.getFlow(id));
    }

    @GetMapping
    public RestResponse<List<ProjectFlow>> getAll() {
        return RestResponse.get(projectFlowService.getFlows(null));
    }

    @GetMapping("/list/{flowName}")
    public RestResponse<List<ProjectFlow>> getByName(@PathVariable("flowName") String flowName) {
        return RestResponse.get(projectFlowService.getFlows(flowName));
    }

}
