package com.sxjkwm.pm.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:message.properties", encoding = "utf-8")
public class MessageConfig {

    @Value("${WXMessage_URL}")
    private String WXMessageURL;

    @Value("${msg_content}")
    private String msgContent;

    public String getWXMessageURL() {
        return WXMessageURL;
    }

    public void setWXMessageURL(String WXMessageURL) {
        this.WXMessageURL = WXMessageURL;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
