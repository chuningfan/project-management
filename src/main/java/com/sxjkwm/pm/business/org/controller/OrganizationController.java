package com.sxjkwm.pm.business.org.controller;

import com.sxjkwm.pm.business.org.dto.OrgDto;
import com.sxjkwm.pm.business.org.service.OrganizationService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/25 15:34
 */
@RestController
@RequestMapping("/org")
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public RestResponse<List<OrgDto>> search(@RequestParam("keyWord") String keyWord) {
        return RestResponse.of(organizationService.getByKey(keyWord));
    }

}
