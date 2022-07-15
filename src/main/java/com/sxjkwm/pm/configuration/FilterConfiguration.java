package com.sxjkwm.pm.configuration;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.context.impl.ContextFactoryImpl;
import com.sxjkwm.pm.auth.context.impl.OpenApiContextFactoryImpl;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.auth.service.OpenAPIAuthService;
import com.sxjkwm.pm.cache.ReqTokenService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.common.XssFilterRequestWrapper;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.ErrorPage;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

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
                                                         @Autowired ContextFactoryImpl userContextFactory, @Autowired WhitelistConfig whitelistConfig,
                                                         @Autowired OpenApiContextFactoryImpl openApiContextFactory, @Autowired OpenAPIAuthService openAPIAuthService) {
        FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        String whitelistStr = whitelistConfig.getWhitelistStr();
        Set<String> legalUris = Sets.newHashSet();
        if (StringUtils.isNotBlank(whitelistStr)) {
            legalUris.addAll(Arrays.asList(whitelistStr.split(";")));
        }
        filterFilterRegistrationBean.setFilter(new AuthFilter(loginService, feConfig, userContextFactory, legalUris, openApiContextFactory, openAPIAuthService));
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
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.setName("GlobalReqTokenFilter");
        return filterFilterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<PmXssFilter> pmXssFilter(@Autowired MultipartResolver multipartResolver) {
        FilterRegistrationBean<PmXssFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new PmXssFilter(multipartResolver));
        filterFilterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        filterFilterRegistrationBean.setOrder(2);
        filterFilterRegistrationBean.setName("PmXssFilter");
        return filterFilterRegistrationBean;
    }

    public static class AuthFilter implements Filter {

        private final LoginService loginService;

        private final FEConfig feConfig;

        private final ContextFactoryImpl userContextFactory;

        private final String loginURL;

        private final Set<String> legalUris;

        private final OpenApiContextFactoryImpl openApiContextFactory;

        private final OpenAPIAuthService openAPIAuthService;

        public AuthFilter(LoginService loginService, FEConfig feConfig, ContextFactoryImpl userContextFactory, Set<String> legalUris, OpenApiContextFactoryImpl openApiContextFactory, OpenAPIAuthService openAPIAuthService) {
            this.loginService = loginService;
            this.feConfig = feConfig;
            this.userContextFactory = userContextFactory;
            loginURL = feConfig.getDomain() + feConfig.getProjectPath() + feConfig.getLoginPath();
            this.legalUris = legalUris;
            this.openApiContextFactory = openApiContextFactory;
            this.openAPIAuthService = openAPIAuthService;
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
            if (!skippable(uri) && !method.equals("OPTIONS")) {
                if (uri.toLowerCase().startsWith(Constant.OPEN_API_FEATURE)) {  // open API auth
                    try {
                        openAPIAuthService.auth(req);
                        filterChain.doFilter(req, resp);
                    } catch (PmException e) {
                        ErrorPage.writeErrorAsJson(resp, e);
                        return;
                    } finally {
                        openApiContextFactory.remove();
                    }
                } else {
                    try {
                        if (!loginService.isValid(req)) {
                            Cookie cookie = new Cookie(LoginService.tokenKey, null);
                            cookie.setMaxAge(0);
                            resp.addCookie(cookie);
                            resp.sendRedirect(loginURL);
                            return;
                        } else {
                            filterChain.doFilter(req, resp);
                        }
                    } finally {
                        userContextFactory.remove();
                    }
                }
            } else {
                filterChain.doFilter(req, resp);
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

    public static class PmXssFilter implements Filter {

        private final MultipartResolver multipartResolver;

        public PmXssFilter(MultipartResolver multipartResolver) {
            this.multipartResolver = multipartResolver;
        }

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }

        @Override
        public void doFilter(ServletRequest request , ServletResponse response , FilterChain chain) throws IOException, ServletException {
            String contentType = request.getContentType();
            if(StringUtils.isNotBlank(contentType) && contentType.contains("multipart/form-data")){
                MultipartHttpServletRequest multipartRequest = multipartResolver.resolveMultipart((HttpServletRequest) request);
                XssFilterRequestWrapper xssRequest = new XssFilterRequestWrapper(multipartRequest);
                chain.doFilter(xssRequest, response);
            }else{
                XssFilterRequestWrapper xssRequest = new XssFilterRequestWrapper((HttpServletRequest)request);
                chain.doFilter(xssRequest, response);
            }
        }

        @Override
        public void destroy() {
        }
    }

}
