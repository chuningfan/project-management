package com.sxjkwm.pm.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:message.properties")
public class MessageConfig {

    @Value("${WXMessage_URL}")
    private String WXMessageURL;

    public String getWXMessageURL() {
        return WXMessageURL;
    }

    public void setWXMessageURL(String WXMessageURL) {
        this.WXMessageURL = WXMessageURL;
    }

}
