package com.sxjkwm.pm.configuration;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.service.LoginService;
import com.sxjkwm.pm.business.file.service.PatternFileService;
import com.sxjkwm.pm.constants.ErrorPage;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<WxLoginServlet> wxLoginServlet(@Autowired LoginService loginService, @Autowired FEConfig feConfig) {
        ServletRegistrationBean<WxLoginServlet> servletServletRegistrationBean = new ServletRegistrationBean<>();
        servletServletRegistrationBean.setUrlMappings(Lists.newArrayList("/wxlogin.do"));
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
                JSONObject res = loginService.doLogin(req);
                String token = loginService.processToken(res, req);
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
    public ServletRegistrationBean<PatternFileDownloadServlet> patternFileDownloadServlet(@Autowired PatternFileService patternFileService) {
        ServletRegistrationBean<PatternFileDownloadServlet> servletServletRegistrationBean = new ServletRegistrationBean<>();
        servletServletRegistrationBean.setUrlMappings(Lists.newArrayList("/pattern.do"));
        servletServletRegistrationBean.setServlet(new PatternFileDownloadServlet(patternFileService));
        servletServletRegistrationBean.setName("WxLoginServlet");
        servletServletRegistrationBean.setOrder(1);
        return servletServletRegistrationBean;
    }

    private static class PatternFileDownloadServlet extends HttpServlet {
        private final PatternFileService patternFileService;
        public PatternFileDownloadServlet(PatternFileService patternFileService) {
            this.patternFileService = patternFileService;
        }
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String filePath = req.getParameter("filePath");
            File file = patternFileService.get(filePath);
            String fileName = file.getName();
            String type = new MimetypesFileTypeMap().getContentType(fileName);
            resp.reset();
            resp.setHeader("content-type", type + ";charset=utf-8");
            resp.setContentType("application/octet-stream;charset=UTF-8");
            resp.addHeader("Content-Length", String.valueOf(file.length()));
            resp.setCharacterEncoding("UTF-8");
            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os;
            try {
                String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
                resp.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
                os = resp.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

}
