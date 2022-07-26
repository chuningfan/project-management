package com.sxjkwm.pm.auditing.controller;

import com.sxjkwm.pm.auditing.service.AuditingRecordService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vic.Chu
 * @date 2022/6/27 10:14
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/auditingRecord")
public class AuditingRecordController {

    private final AuditingRecordService auditingRecordService;

    @Autowired
    public AuditingRecordController(AuditingRecordService auditingRecordService) {
        this.auditingRecordService = auditingRecordService;
    }

    @GetMapping("/{auditingRecordId}")
    public RestResponse<String> readAuditingRecord(@PathVariable("auditingRecordId") Long auditingRecordId) throws PmException {
        return RestResponse.of(auditingRecordService.getAuditingDataJSONString(auditingRecordId));
    }

}
