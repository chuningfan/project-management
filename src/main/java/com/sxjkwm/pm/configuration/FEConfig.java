package com.sxjkwm.pm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:FEConfig.properties")
public class FEConfig {

    @Value("${fe.domain}")
    private String domain;

    @Value("${fe.projectPath}")
    private String projectPath;

    @Value("${fe.page.loginPath}")
    private String loginPath;

    @Value("${fe.page.redirectPath}")
    private String redirectPath;

    @Value("${fe.page.indexPath}")
    private String indexPath;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public void setRedirectPath(String redirectPath) {
        this.redirectPath = redirectPath;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

}
