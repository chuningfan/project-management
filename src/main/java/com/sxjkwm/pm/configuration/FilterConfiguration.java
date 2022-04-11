package com.sxjkwm.pm.configuration;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.context.impl.ContextFactoryImpl;
import com.sxjkwm.pm.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Configuration
public class FilterConfiguration {

    private static final List<String> whitelist = Lists.newArrayList("/loginData", "/wxlogin.do");

    private static AntPathMatcher matcher = new AntPathMatcher();

    @Bean
    public FilterRegistrationBean<AuthFilter> authFilter(@Autowired LoginService loginService, @Autowired FEConfig feConfig, @Autowired ContextFactory contextFactory) {
        FilterRegistrationBean<AuthFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new AuthFilter(loginService, feConfig, contextFactory));
        filterFilterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        filterFilterRegistrationBean.setOrder(0);
        filterFilterRegistrationBean.setName("GlobalAuthFilter");
        return filterFilterRegistrationBean;
    }



    public static class AuthFilter implements Filter {

        private final LoginService loginService;

        private final FEConfig feConfig;

        private final ContextFactory<ContextFactoryImpl.AuthUser> contextFactory;

        private final String loginURL;

        public AuthFilter(LoginService loginService, FEConfig feConfig, ContextFactory<ContextFactoryImpl.AuthUser> contextFactory) {
            this.loginService = loginService;
            this.feConfig = feConfig;
            this.contextFactory = contextFactory;
            loginURL = feConfig.getDomain() + feConfig.getProjectPath() + feConfig.getLoginPath();
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
            for (String pattern: whitelist) {
                if (matcher.match(pattern, uri)) {
                    return true;
                }
            }
            return false;
        }

    }

}
