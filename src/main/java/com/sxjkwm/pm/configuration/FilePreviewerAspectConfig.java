package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.auth.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Configuration
@Aspect
public class FilePreviewerAspectConfig {

    private static final String suffix = "_";

    @Before("@annotation(com.sxjkwm.pm.configuration.annotation.PreviewFile)")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = pickUpReq(args);
        if (Objects.isNull(request)) {
            return;
        }
        String nowCookie = request.getParameter(LoginService.tokenKey);
        if (StringUtils.isBlank(nowCookie)) {
            return;
        }
        HttpServletResponse response = pickUpResp(args);
        if (Objects.isNull(response)) {
            return;
        }
        Cookie cookie = new Cookie(LoginService.tokenKey + suffix, nowCookie);
        cookie.setPath("/");
        cookie.setMaxAge(5 * 60 * 60);
        response.addCookie(cookie);
    }

    private HttpServletRequest pickUpReq(Object[] args) {
        for (Object arg: args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }

    private HttpServletResponse pickUpResp(Object[] args) {
        for (Object arg: args) {
            if (arg instanceof HttpServletResponse) {
                return (HttpServletResponse) arg;
            }
        }
        return null;
    }

}
