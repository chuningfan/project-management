package com.sxjkwm.pm.business.eplatform.controller;

import com.google.common.base.Joiner;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.eplatform.dto.BusinessResponsibilityDto;
import com.sxjkwm.pm.business.eplatform.service.BusinessResponsibilityService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/8/23 16:45
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/ep-bizResp")
public class EpBizResonsibilityController {

    private final BusinessResponsibilityService businessResponsibilityService;

    @Autowired
    public EpBizResonsibilityController(BusinessResponsibilityService businessResponsibilityService) {
        this.businessResponsibilityService = businessResponsibilityService;
    }

    @PostMapping
    public RestResponse<Long> saveOrUpdate(@RequestBody List<String> orgs) {
        String data = Joiner.on(",").join(orgs);
        BusinessResponsibilityDto dto = new BusinessResponsibilityDto();
        dto.setOwnerId(ContextHelper.getUserData().getWxUserId());
        dto.setBuyerOrgs(data);
        return RestResponse.of(businessResponsibilityService.saveOrUpdate(dto));
    }

    @GetMapping
    public RestResponse<List<String>> query() {
        return RestResponse.of(businessResponsibilityService.get());
    }


}
