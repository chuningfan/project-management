package com.sxjkwm.pm.business.centralizedpurchase.controller;

import com.sxjkwm.pm.business.centralizedpurchase.dto.BidInfoDto;
import com.sxjkwm.pm.business.centralizedpurchase.service.BidInfoService;
import com.sxjkwm.pm.business.centralizedpurchase.service.ProjectInfoService;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/7/4 16:59
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/cp")
public class CentralizedPurchaseController {

    private final ProjectInfoService projectInfoService;

    private final BidInfoService bidInfoService;

    public CentralizedPurchaseController(ProjectInfoService projectInfoService, BidInfoService bidInfoService) {
        this.projectInfoService = projectInfoService;
        this.bidInfoService = bidInfoService;
    }

    @GetMapping("projectInfo")
    public RestResponse<ProjectDto> findByProjectCode(@RequestParam("projectCode") String projectCode) {
        return RestResponse.of(projectInfoService.findProjectInfoByTaskCode(projectCode));
    }

    @GetMapping("/codes")
    public RestResponse<List<String>> queryProjectCodes() {
        return RestResponse.of(projectInfoService.queryProjectCodes());
    }

    @GetMapping("/bidInfo")
    public RestResponse<List<BidInfoDto>> queryBidInfo(@RequestParam("projectCode") String projectCode) {
        return RestResponse.of(bidInfoService.queryBidInfoByProjectCode(projectCode));
    }

}
