package com.sxjkwm.pm.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/wxworkLogin")
public class LoginCallbackController {

    private final LoginService loginService;

    @Autowired
    public LoginCallbackController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public void wxworkLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException, PmException {
        try {
            JSONObject res = loginService.doLogin(req);
            String token = loginService.processToken(res, req);
            resp.sendRedirect("http://localhost:8080/pm/redirect.html?" + LoginService.tokenKey + "=" + token);
        } catch (PmException e) {
            String code = e.getCode();
            if (PmError.WXWORK_LOGIN_FAILED.getValue().equals(code)) {
                resp.sendRedirect("http://localhost:8080/pm/login.html");
            }
            throw e;
        }
    }

}
