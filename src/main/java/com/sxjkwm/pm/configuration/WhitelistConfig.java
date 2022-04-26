package com.sxjkwm.pm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:whitelist.properties")
public class WhitelistConfig {
    @Value("${whitelist}")
    private String whitelistStr;

    public String getWhitelistStr() {
        return whitelistStr;
    }

    public void setWhitelistStr(String whitelistStr) {
        this.whitelistStr = whitelistStr;
    }

}
