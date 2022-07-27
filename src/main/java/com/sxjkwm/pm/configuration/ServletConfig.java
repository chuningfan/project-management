package com.sxjkwm.pm.configuration;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.ErrorPage;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class ServletConfig {

    private static final String userIdKey = "userid";

    @Bean
    public ServletRegistrationBean<WxLoginServlet> wxLoginServlet(@Autowired LoginService loginService, @Autowired FEConfig feConfig) {
        ServletRegistrationBean<WxLoginServlet> servletServletRegistrationBean = new ServletRegistrationBean<>();
        servletServletRegistrationBean.setUrlMappings(Lists.newArrayList(Constant.API_FEATURE + "/wxlogin.do"));
        servletServletRegistrationBean.setServlet(new WxLoginServlet(loginService, feConfig));
        servletServletRegistrationBean.setName("WxLoginServlet");
        servletServletRegistrationBean.setOrder(0);
        return servletServletRegistrationBean;
    }

    private static class WxLoginServlet extends HttpServlet {

        private final LoginService loginService;

        private final FEConfig feConfig;

        private final String redirectURL;

        private final String loginURL;

        private WxLoginServlet(LoginService loginService, FEConfig feConfig) {
            this.loginService = loginService;
            this.feConfig = feConfig;
            redirectURL = feConfig.getDomain() + feConfig.getProjectPath() + feConfig.getRedirectPath();
            loginURL = feConfig.getDomain() + feConfig.getProjectPath() + feConfig.getLoginPath();
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            try {
                String wxUserId = loginService.doLogin(req);
                String token = loginService.processToken(wxUserId);
                resp.sendRedirect(redirectURL + "?" + LoginService.tokenKey + "=" + token);
            } catch (PmException e) {
                String code = e.getCode();
                if (PmError.WXWORK_LOGIN_FAILED.getValue().equals(code)) {
                    resp.sendRedirect(loginURL);
                } else {
                    PrintWriter writer = resp.getWriter();
                    writer.write(ErrorPage.getErrorPageHtml(e.getErrorMessage()));
                    writer.flush();
                    writer.close();
                }
            }
        }
    }

    @Bean
    public ServletRegistrationBean<PatternFileDownloadServlet> patternFileDownloadServlet() {
        ServletRegistrationBean<PatternFileDownloadServlet> servletServletRegistrationBean = new ServletRegistrationBean<>();
        servletServletRegistrationBean.setUrlMappings(Lists.newArrayList("/patternFileDownload.do"));
        servletServletRegistrationBean.setServlet(new PatternFileDownloadServlet());
        servletServletRegistrationBean.setName("PatternFileDownloadServlet");
        servletServletRegistrationBean.setOrder(1);
        return servletServletRegistrationBean;
    }

    private static class PatternFileDownloadServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String filePath = req.getParameter("filePath");
            FileUtil.download(resp, filePath);
        }
    }

}
