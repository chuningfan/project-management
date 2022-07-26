package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.dto.ExternalEnterpriseRegisterDto;
import com.sxjkwm.pm.auth.service.OpenAPIAuthService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/15 14:53
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/externalEnterprise")
public class ExternalEnterpriseController {

    private final OpenAPIAuthService openAPIAuthService;

    @Autowired
    public ExternalEnterpriseController(OpenAPIAuthService openAPIAuthService) {
        this.openAPIAuthService = openAPIAuthService;
    }

    @PostMapping
    public RestResponse<Map<String, String>> register(@RequestBody ExternalEnterpriseRegisterDto externalEnterpriseRegisterDto) throws PmException {
        return RestResponse.of(openAPIAuthService.generateBaseInfo(externalEnterpriseRegisterDto.getEnterpriseName(), externalEnterpriseRegisterDto.getDeadline()));
    }

}
