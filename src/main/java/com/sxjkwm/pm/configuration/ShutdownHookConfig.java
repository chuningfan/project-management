package com.sxjkwm.pm.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * @author Vic.Chu
 * @date 2022/8/2 14:55
 */
@Configuration
public class ShutdownHookConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHookConfig.class);

    @PreDestroy
    public void doShutdown() {
        logger.info("Spring Container is being closed");
    }

}
