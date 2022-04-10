package com.sxjkwm.pm.business.controller;

import com.sxjkwm.pm.business.dto.ProjectFlowNodeDto;
import com.sxjkwm.pm.business.entity.ProjectFlowNode;
import com.sxjkwm.pm.business.service.ProjectFlowNodeService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project-flow-node")
public class ProjectFlowNodeController {

    private final ProjectFlowNodeService projectFlowNodeService;

    @Autowired
    public ProjectFlowNodeController(ProjectFlowNodeService projectFlowNodeService) {
        this.projectFlowNodeService = projectFlowNodeService;
    }

    @PostMapping("/{flowId}")
    public RestResponse<List<ProjectFlowNodeDto>> createNodes(@PathVariable("flowId") Long flowId, @RequestBody List<ProjectFlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.get(projectFlowNodeService.create(flowId, projectFlowNodeDtos));
    }

    @PutMapping("/{flowId}")
    public RestResponse<List<ProjectFlowNodeDto>> updateNodes(@PathVariable("flowId") Long flowId, @RequestBody List<ProjectFlowNodeDto> projectFlowNodeDtos) {
        return RestResponse.get(projectFlowNodeService.update(flowId, projectFlowNodeDtos));
    }

    @GetMapping("/flowId")
    public RestResponse<List<ProjectFlowNode>> getNodes(@PathVariable("flowId") Long flowId) {
        return RestResponse.get(projectFlowNodeService.getNodes(flowId));
    }

}
