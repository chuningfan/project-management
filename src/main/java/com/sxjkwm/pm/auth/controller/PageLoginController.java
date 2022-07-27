package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.dto.PageLoginFormDto;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Vic.Chu
 * @date 2022/7/27 17:18
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/pageLogin")
public class PageLoginController {

    private final LoginService loginService;

    @Autowired
    public PageLoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public RestResponse<String> doLogin(@RequestBody PageLoginFormDto pageLoginFormDto, HttpServletResponse response) throws PmException {
        return RestResponse.of(loginService.doLogin(pageLoginFormDto.getUsername(), pageLoginFormDto.getPassword(), pageLoginFormDto.getCaptcha(), response));
    }

    @PutMapping("/credentialsModification")
    public RestResponse<Boolean> updatePassword(@RequestBody PageLoginFormDto pageLoginFormDto, HttpServletResponse response) throws PmException {
        return RestResponse.of(loginService.updatePassword(pageLoginFormDto.getUsername(), pageLoginFormDto.getOldPassword(), pageLoginFormDto.getPassword(), pageLoginFormDto.getOldPassword(), response));
    }

}
