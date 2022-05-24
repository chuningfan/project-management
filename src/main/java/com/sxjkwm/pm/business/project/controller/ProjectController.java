package com.sxjkwm.pm.business.project.controller;

import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.common.BaseController;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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

    @GetMapping("/list")
    public RestResponse<Page<Project>> getMyProjects(@RequestParam(name = "status", required = false) Integer status, @RequestParam(name = "projectName", required = false) String projectName,
                                                     @RequestParam(name = "projectCode", required = false)String projectCode, @RequestParam(name = "requirePart", required = false) String requirePart,
                                                     @RequestParam("pageNo") Integer pageNo, @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (Objects.isNull(pageSize)) {
            pageSize = 15;
        }
        if (Objects.nonNull(status) && -1 == status.intValue()) {
            status = null;
        }
        UserDataDto userDataDto = getUserData();
        if (Objects.isNull(userDataDto)) {
            return RestResponse.of(projectService.queryMine("chuningfan", status, projectCode, projectName, requirePart, pageNo, pageSize));
        }
        return RestResponse.of(projectService.queryMine(userDataDto.getWxUserId(), status, projectCode, projectName, requirePart, pageNo, pageSize));
    }

}
