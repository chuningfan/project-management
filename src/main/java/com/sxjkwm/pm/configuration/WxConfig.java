package com.sxjkwm.pm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/wxworkConfig/wxworkConfig-${spring.profiles.active}.properties")
public class WxConfig {

    @Value("${wx.corpId}")
    private String corpId;

    @Value("${wx.agentId}")
    private String agentId;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.tokenURL}")
    private String tokenURL;

    @Value("${wx.loginCodeURL}")
    private String loginCodeURL;

    @Value("${wx.userURL}")
    private String userURL;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTokenURL() {
        return tokenURL;
    }

    public void setTokenURL(String tokenURL) {
        this.tokenURL = tokenURL;
    }

    public String getLoginCodeURL() {
        return loginCodeURL;
    }

    public void setLoginCodeURL(String loginCodeURL) {
        this.loginCodeURL = loginCodeURL;
    }

    public String getUserURL() {
        return userURL;
    }

    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }
}
