package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.service.UserDataService;
import com.sxjkwm.pm.common.BaseController;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constant.API_FEATURE + "/userData")
public class UserDataController extends BaseController {

    private final UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/person/{wxUserId}")
    public RestResponse<UserDataDto> findUserDataByWxUserId(@PathVariable("wxUserId") String wxUserId) {
        return RestResponse.of(userDataService.getUserDataByWxUserId(wxUserId));
    }

    @GetMapping
    public RestResponse<UserDataDto> findUserDataByToken() {
        return RestResponse.of(getUserData());
    }

    @GetMapping("/list")
    public RestResponse<List<UserDataDto>> findUserDataList() {
        return RestResponse.of(userDataService.fetchAvailableUsers());
    }

}
