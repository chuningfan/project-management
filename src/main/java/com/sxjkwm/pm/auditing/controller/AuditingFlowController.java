package com.sxjkwm.pm.auditing.controller;

import com.sxjkwm.pm.auditing.dto.AuditingFlowDto;
import com.sxjkwm.pm.auditing.service.AuditingFlowService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/27 9:05
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/auditingFlow")
public class AuditingFlowController {

    private final AuditingFlowService auditingFlowService;

    @Autowired
    public AuditingFlowController(AuditingFlowService auditingFlowService) {
        this.auditingFlowService = auditingFlowService;
    }

    @GetMapping("/list")
    public RestResponse<List<AuditingFlowDto>> queryAvailableFlows() {
        return RestResponse.of(auditingFlowService.findAvailableFlows());
    }

}
