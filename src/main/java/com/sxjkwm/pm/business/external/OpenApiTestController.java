package com.sxjkwm.pm.business.external;

import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vic.Chu
 * @date 2022/7/15 15:16
 */
//@RestController
//@RequestMapping(Constant.OPEN_API_FEATURE)
//public class OpenApiTestController {
//
//    @GetMapping("/test")
//    public RestResponse<String> test() {
//        ExternalRPCDataDto externalRPCDataDto = ContextHelper.getExternalRpcData();
//        return RestResponse.of(externalRPCDataDto.getAppKey() + "-" + externalRPCDataDto.getReqIp());
//    }
//
//}
