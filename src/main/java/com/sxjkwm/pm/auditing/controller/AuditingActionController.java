package com.sxjkwm.pm.auditing.controller;

import com.sxjkwm.pm.auditing.dto.StartFlowDto;
import com.sxjkwm.pm.auditing.service.AuditingActionService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vic.Chu
 * @date 2022/6/27 9:13
 */
@RestController
@RequestMapping("/auditingAction")
public class AuditingActionController {

    private final AuditingActionService auditingActionService;

    @Autowired
    public AuditingActionController(AuditingActionService auditingActionService) {
        this.auditingActionService = auditingActionService;
    }

    @PostMapping
    public RestResponse<Boolean> startFlow(@RequestBody StartFlowDto startFlowDto) throws PmException {
        return RestResponse.of(auditingActionService.start(startFlowDto.getAuditingDataType(), startFlowDto.getDataId(), startFlowDto.getAuditingFlowId(), startFlowDto.getBusinessFlowNodeId(), startFlowDto.getOnlySave()));
    }

}
