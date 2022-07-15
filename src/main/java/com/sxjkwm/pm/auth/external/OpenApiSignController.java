package com.sxjkwm.pm.auth.external;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.auth.service.OpenAPIAuthService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vic.Chu
 * @date 2022/7/15 14:39
 */
@RestController
@RequestMapping("/openApi")
public class OpenApiSignController {

    private final OpenAPIAuthService openAPIAuthService;

    @Autowired
    public OpenApiSignController(OpenAPIAuthService openAPIAuthService) {
        this.openAPIAuthService = openAPIAuthService;
    }

    @PostMapping("/sign")
    public RestResponse<String> getSign(@RequestBody JSONObject jsonObject) throws PmException {
        String appKey = jsonObject.getString("appKey");
        String appSecret = jsonObject.getString("appSecret");
        return RestResponse.of(openAPIAuthService.getSign(appKey, appSecret));
    }





}
