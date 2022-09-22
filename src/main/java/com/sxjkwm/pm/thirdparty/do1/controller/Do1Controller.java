package com.sxjkwm.pm.thirdparty.do1.controller;

import com.sxjkwm.pm.business.project.handler.AuditingFormDataHandler;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.thirdparty.do1.dto.PropertyMappingDto;
import com.sxjkwm.pm.thirdparty.do1.entitiy.FormMapping;
import com.sxjkwm.pm.thirdparty.do1.entitiy.PropertyMapping;
import com.sxjkwm.pm.business.flow.handler.service.Do1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/9/16 9:21
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/do1Api")
public class Do1Controller {

    private final Do1Service do1Service;

    private final AuditingFormDataHandler auditingFormDataHandler;

    @Autowired
    public Do1Controller(Do1Service do1Service, AuditingFormDataHandler auditingFormDataHandler) {
        this.do1Service = do1Service;
        this.auditingFormDataHandler = auditingFormDataHandler;
    }

    @GetMapping("/syncIds")
    public RestResponse<Boolean> syncIds() {
        return RestResponse.of(do1Service.syncIds());
    }

    @GetMapping("/forms")
    public RestResponse<List<FormMapping>> fetchAvailableForms() {
        return RestResponse.of(do1Service.fetchAvailableForms());
    }

    @PostMapping("/propertyMapping")
    public RestResponse<List<PropertyMapping>> saveOrUpdate(@RequestBody List<PropertyMappingDto> mappings) {
        return RestResponse.of(do1Service.saveOrUpdatePropertyMapping(mappings));
    }

    @GetMapping("/propertyMapping")
    public RestResponse<List<PropertyMappingDto>> findPropertyMappingByFlowNodeId(@RequestParam("flowNodeId") Long flowNodeId) {
        return RestResponse.of(do1Service.findPropertyMappingByFlowNodeId(flowNodeId));
    }

}
