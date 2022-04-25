package com.sxjkwm.pm;

import cn.hutool.core.date.StopWatch;
import com.sxjkwm.pm.util.ContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ProjectManagementStarter {

    private static final Logger logger = LoggerFactory.getLogger(ProjectManagementStarter.class);

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        logger.info("We are going to start up the service");
        stopWatch.start();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ProjectManagementStarter.class);
        builder.listeners((ApplicationListener<ContextRefreshedEvent>) contextRefreshedEvent -> {
            ContextUtil.context = contextRefreshedEvent.getApplicationContext();
            logger.info("Spring is fully started, context-util is ready!");
        });
        builder.run(args);
        stopWatch.stop();
        logger.info("========= Service is fully started! ========= cost: " + stopWatch.getTotalTimeSeconds() + "seconds");
    }

}
