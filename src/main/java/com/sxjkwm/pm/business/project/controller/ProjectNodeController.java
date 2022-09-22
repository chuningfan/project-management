package com.sxjkwm.pm.business.project.controller;

import cn.com.do1.apisdk.inter.rep.vo.ApiFormPushResult;
import com.sxjkwm.pm.business.project.dto.AuditingFormRequest;
import com.sxjkwm.pm.business.project.dto.ProjectNodeDto;
import com.sxjkwm.pm.business.project.handler.AuditingFormDataHandler;
import com.sxjkwm.pm.business.project.service.ProjectNodeService;
import com.sxjkwm.pm.common.BaseController;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author Vic.Chu
 * @date 2022/5/15 16:37
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/projectNode")
public class ProjectNodeController extends BaseController {

    private final ProjectNodeService projectNodeService;

    private final AuditingFormDataHandler auditingFormDataHandler;

    @Autowired
    public ProjectNodeController(ProjectNodeService projectNodeService, AuditingFormDataHandler auditingFormDataHandler) {
        this.projectNodeService = projectNodeService;
        this.auditingFormDataHandler = auditingFormDataHandler;
    }

    @PostMapping
    public RestResponse<ProjectNodeDto> saveOrUpdate(@RequestBody ProjectNodeDto projectNodeDto) throws PmException, SQLException {
        return RestResponse.of(projectNodeService.saveOrUpdate(projectNodeDto));
    }

    @GetMapping("/detail")
    public RestResponse<ProjectNodeDto> getOne(@RequestParam("projectId") Long projectId, @RequestParam("flowNodeId") Long flowNodeId) throws SQLException {
        return RestResponse.of(projectNodeService.getOne(projectId, flowNodeId));
    }

    @DeleteMapping("/{flowId}/{flowNodeId}/{flowNodeDefinitionId}/{dataId}")
    public RestResponse<Boolean> deleteDataById(@PathVariable("flowId") Long flowId, @PathVariable("flowNodeId") Long flowNodeId, @PathVariable("flowNodeDefinitionId") Long flowNodeDefinitionId, @PathVariable("dataId") Long dataId) {
        return RestResponse.of(projectNodeService.deleteDataById(flowId, flowNodeId, flowNodeDefinitionId, dataId));
    }

    @PostMapping("/auditing")
    public RestResponse<ApiFormPushResult> auditing(@RequestBody AuditingFormRequest request) throws Throwable {
        return RestResponse.of(auditingFormDataHandler.doHandle(request.getProjectId(), request.getFlowId(), request.getFlowNodeId(), request.getFormId(), request.getSpecialFormDataHandler(), request.getCommand()));
    }

}
