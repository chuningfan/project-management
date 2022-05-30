package com.sxjkwm.pm.configuration;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.cache.ReqTokenService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.PmError;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;

@Configuration
public class FilterConfiguration {

    private static AntPathMatcher matcher = new AntPathMatcher();

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(@Autowired LoginService loginService, @Autowired FEConfig feConfig,
                                                         @Autowired ContextFactory contextFactory, @Autowired WhitelistConfig whitelistConfig) {
        FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        String whitelistStr = whitelistConfig.getWhitelistStr();
        Set<String> legalUris = Sets.newHashSet();
        if (StringUtils.isNotBlank(whitelistStr)) {
            legalUris.addAll(Arrays.asList(whitelistStr.split(";")));
        }
        filterFilterRegistrationBean.setFilter(new AuthFilter(loginService, feConfig, contextFactory, legalUris));
        filterFilterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        filterFilterRegistrationBean.setOrder(0);
        filterFilterRegistrationBean.setName("GlobalAuthFilter");
        return filterFilterRegistrationBean;
    }


    @Bean
    public FilterRegistrationBean<ReqTokenFilter> reqTokenFilter(@Autowired ReqTokenService reqTokenService,
                                                                 @Autowired ReqTokenUrisConfig reqTokenUrisConfig,
                                                                 @Autowired WhitelistConfig whitelistConfig) {
        String whitelistStr = whitelistConfig.getWhitelistStr();
        Set<String> legalUris = Sets.newHashSet();
        if (StringUtils.isNotBlank(whitelistStr)) {
            legalUris.addAll(Arrays.asList(whitelistStr.split(";")));
        }
        FilterRegistrationBean<ReqTokenFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new ReqTokenFilter(reqTokenService, reqTokenUrisConfig, legalUris));
        filterFilterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        filterFilterRegistrationBean.setOrder(0);
        filterFilterRegistrationBean.setName("GlobalReqTokenFilter");
        return filterFilterRegistrationBean;
    }


    @Order(1)
    public static class AuthFilter implements Filter {

        private final LoginService loginService;

        private final FEConfig feConfig;

        private final ContextFactory<UserDataDto> contextFactory;

        private final String loginURL;

        private final Set<String> legalUris;

        public AuthFilter(LoginService loginService, FEConfig feConfig, ContextFactory<UserDataDto> contextFactory, Set<String> legalUris) {
            this.loginService = loginService;
            this.feConfig = feConfig;
            this.contextFactory = contextFactory;
            loginURL = feConfig.getDomain() + feConfig.getProjectPath() + feConfig.getLoginPath();
            this.legalUris = legalUris;
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            String method = req.getMethod();
            String uri = req.getRequestURI();
            try {
                if (!skippable(uri) && !method.equals("OPTIONS")) {
                    if (!loginService.isValid(req)) {
                        Cookie cookie = new Cookie(LoginService.tokenKey, null);
                        cookie.setMaxAge(0);
                        resp.addCookie(cookie);
                        resp.sendRedirect(loginURL);
                        return;
                    }
                }
                filterChain.doFilter(req, resp);
            } finally {
                contextFactory.remove();
            }
        }

        @Override
        public void destroy() {
        }

        private boolean skippable(String uri) {
            if (CollectionUtils.isEmpty(legalUris)) {
                return false;
            }
            for (String pattern: legalUris) {
                if (matcher.match(pattern, uri)) {
                    return true;
                }
            }
            return false;
        }

    }

    @Order(2)
    public class ReqTokenFilter implements Filter {

        public static final String reqTokenKey = "reqToken";

        private final ReqTokenService reqTokenService;

        private final ReqTokenUrisConfig reqTokenUrisConfig;

        private final Set<String> targetUris;

        private final Set<String> legalUris;

        public ReqTokenFilter(ReqTokenService reqTokenService, ReqTokenUrisConfig reqTokenUrisConfig, Set<String> legalUris) {
            this.reqTokenService = reqTokenService;
            this.reqTokenUrisConfig = reqTokenUrisConfig;
            this.legalUris = legalUris;
            String uriList = reqTokenUrisConfig.getReqTokenUris();
            targetUris = Sets.newHashSet();
            if (StringUtils.isNotBlank(uriList)) {
                for (String uri: Splitter.on(";").split(uriList)) {
                    targetUris.add(uri);
                }
            }
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                             FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            String reqUri = req.getRequestURI();
            String method = req.getMethod();
            if (skippable(reqUri) || method.equals("OPTIONS")) {
                filterChain.doFilter(req, resp);
            } else {
                String reqToken = req.getHeader(reqTokenKey);
                String _reqToken = reqTokenService.getToken();
                if (StringUtils.isNotBlank(_reqToken) && _reqToken.equals(reqToken)) {
                    filterChain.doFilter(req, resp);
                } else {
                    _reqToken = reqTokenService.generateToken();
                    RestResponse<String> restResponse = RestResponse.of(_reqToken);
                    restResponse.setCode(PmError.ILLEGAL_REQUEST_TOKEN.getValue());
                    restResponse.setMessage(PmError.ILLEGAL_REQUEST_TOKEN.getLabel());
                    String respData = JSONObject.toJSONString(restResponse);
                    resp.setContentType("application/json");
                    try (PrintWriter writer = resp.getWriter()) {
                        writer.write(respData);
                        writer.flush();
                    }
                }
            }
        }

        @Override
        public void destroy() {
        }

        private boolean skippable(String uri) {
            if (CollectionUtils.isNotEmpty(legalUris)) {
                for (String pattern: legalUris) {
                    if (matcher.match(pattern, uri)) {
                        return true;
                    }
                }
            }
            if (CollectionUtils.isEmpty(targetUris)) {
                return true;
            }
            for (String pattern: targetUris) {
                if (matcher.match(pattern, uri)) {
                    return false;
                }
            }
            return true;
        }
    }

}
