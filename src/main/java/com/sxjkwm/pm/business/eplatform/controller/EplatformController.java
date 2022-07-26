package com.sxjkwm.pm.business.eplatform.controller;

import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.eplatform.dto.BillCheckingDto;
import com.sxjkwm.pm.business.eplatform.service.EplatformService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/25 10:08
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/ep")
public class EplatformController {

    private final EplatformService eplatformService;

    public EplatformController(EplatformService eplatformService) {
        this.eplatformService = eplatformService;
    }

    @GetMapping("/billCheckingList")
    public RestResponse<List<BillCheckingDto>> queryBillCheckingList(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> conditionMap = Maps.newHashMap();
        while (paramNames.hasMoreElements()) {
            String paramKey = paramNames.nextElement();
            conditionMap.put(paramKey, request.getParameter(paramKey));
        }
        return RestResponse.of(eplatformService.queryData(conditionMap));
    }

}
