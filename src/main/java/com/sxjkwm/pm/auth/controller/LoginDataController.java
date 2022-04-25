package com.sxjkwm.pm.auth.controller;

import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/loginData")
public class LoginDataController {

    private final LoginService loginService;

    @Autowired
    public LoginDataController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public RestResponse<Boolean> validate(HttpServletRequest req) {
        return RestResponse.of(loginService.isValid(req));
    }

}
