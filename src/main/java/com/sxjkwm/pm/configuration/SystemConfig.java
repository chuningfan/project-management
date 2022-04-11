package com.sxjkwm.pm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:SystemConfig.properties")
public class SystemConfig {

    @Value("${file.pattern.dir}")
    private String patternFilePath;

    public String getPatternFilePath() {
        return patternFilePath;
    }

    public void setPatternFilePath(String patternFilePath) {
        this.patternFilePath = patternFilePath;
    }
}
