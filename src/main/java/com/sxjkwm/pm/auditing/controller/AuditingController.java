package com.sxjkwm.pm.auditing.controller;

import com.sxjkwm.pm.auditing.entity.AuditingRecord;
import com.sxjkwm.pm.auditing.service.AuditingActionService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/21 12:06
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/auditing")
public class AuditingController {

    private final AuditingActionService auditingActionService;

    @Autowired
    public AuditingController(AuditingActionService auditingActionService) {
        this.auditingActionService = auditingActionService;
    }

    @GetMapping("/list")
    public RestResponse<List<AuditingRecord>> queryAuditable(@RequestParam(value = "auditingFlowId", required = false) Long auditingFlowId) {
        return RestResponse.of(auditingActionService.queryAuditableRecordsByAuditingFlowId(auditingFlowId));
    }

}
