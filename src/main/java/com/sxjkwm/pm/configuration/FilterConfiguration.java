package com.sxjkwm.pm.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.common.AuthUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Configuration
public class FilterConfiguration {

    private static AntPathMatcher matcher = new AntPathMatcher();

//    @Bean
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



    public static class AuthFilter implements Filter {

        private final LoginService loginService;

        private final FEConfig feConfig;

        private final ContextFactory<AuthUser> contextFactory;

        private final String loginURL;

        private final Set<String> legalUris;

        public AuthFilter(LoginService loginService, FEConfig feConfig, ContextFactory<AuthUser> contextFactory, Set<String> legalUris) {
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
            String uri = req.getRequestURI();
            try {
                if (!skippable(uri)) {
                    if (!loginService.isValid(req)) {
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

}
