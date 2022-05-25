package com.sxjkwm.pm.business.project.controller;

import com.sxjkwm.pm.business.project.dto.ProjectNodeDto;
import com.sxjkwm.pm.business.project.service.ProjectNodeService;
import com.sxjkwm.pm.common.BaseController;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author Vic.Chu
 * @date 2022/5/15 16:37
 */
@RestController
@RequestMapping("/projectNode")
public class ProjectNodeController extends BaseController {

    private final ProjectNodeService projectNodeService;

    @Autowired
    public ProjectNodeController(ProjectNodeService projectNodeService) {
        this.projectNodeService = projectNodeService;
    }

    @PostMapping
    public RestResponse<ProjectNodeDto> saveOrUpdate(@RequestBody ProjectNodeDto projectNodeDto) throws PmException, SQLException {
        return RestResponse.of(projectNodeService.saveOrUpdate(projectNodeDto));
    }

    @GetMapping("/detail")
    public RestResponse<ProjectNodeDto> getOne(@RequestParam("projectId") Long projectId, @RequestParam("flowNodeId") Long flowNodeId) throws SQLException {
        return RestResponse.of(projectNodeService.getOne(projectId, flowNodeId));
    }

}
