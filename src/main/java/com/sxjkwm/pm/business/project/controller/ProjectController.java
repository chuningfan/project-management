package com.sxjkwm.pm.business.project.controller;

import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.common.BaseController;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public RestResponse<ProjectDto> saveOrUpdate(@RequestBody ProjectDto projectDto) {
        return RestResponse.of(projectService.saveOrUpdate(projectDto));
    }

}
