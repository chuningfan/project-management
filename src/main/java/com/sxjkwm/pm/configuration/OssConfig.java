package com.sxjkwm.pm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Vic.Chu
 * @date 2022/5/13 20:45
 */
@Configuration
@PropertySource("classpath:OSSConfig.properties")
public class OssConfig {

    @Value("${oss.address}")
    private String ossAddress;

    @Value("${oss.secretKey}")
    private String secretKey;

    @Value("${oss.accessKey}")
    private String accessKey;

    public String getOssAddress() {
        return ossAddress;
    }

    public void setOssAddress(String ossAddress) {
        this.ossAddress = ossAddress;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
