package com.sxjkwm.pm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Vic.Chu
 * @date 2022/5/30 8:59
 */

@Configuration
@PropertySource("classpath:ReqTokenConfigUris.properties")
public class ReqTokenUrisConfig {

    @Value("${reqTokenUriList}")
    private String reqTokenUris;

    public void setReqTokenUris(String reqTokenUris) {
        this.reqTokenUris = reqTokenUris;
    }

    public String getReqTokenUris() {
        return reqTokenUris;
    }
}
