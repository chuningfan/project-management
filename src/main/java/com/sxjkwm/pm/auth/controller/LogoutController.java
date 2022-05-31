package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final LoginService loginService;

    @Autowired
    public LogoutController(LoginService loginService) {
        this.loginService = loginService;
    }


    @GetMapping
    public RestResponse<Boolean> logout() {
        return RestResponse.of(loginService.logout());
    }

}
