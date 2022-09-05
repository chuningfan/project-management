package com.sxjkwm.pm;

import cn.hutool.core.date.StopWatch;
import com.sxjkwm.pm.business.material.service.MaterialCacheService;
import com.sxjkwm.pm.configuration.ValveAspectConfig;
import com.sxjkwm.pm.logging.LoggingAfterInvocation;
import com.sxjkwm.pm.logging.LoggingBeforeInvocation;
import com.sxjkwm.pm.util.ContextUtil;
import com.sxjkwm.pm.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class ProjectManagementStarter {

    private static final Logger logger = LoggerFactory.getLogger(ProjectManagementStarter.class);

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        logger.info("We are going to start up the service");
        stopWatch.start();
        SpringApplicationBuilder builder = new SpringApplicationBuilder(ProjectManagementStarter.class);
        // setup something
        builder.listeners((ApplicationListener<ContextRefreshedEvent>) contextRefreshedEvent -> {
            ContextUtil.context = contextRefreshedEvent.getApplicationContext();
            ValveAspectConfig aspectConfig = ContextUtil.getBean(ValveAspectConfig.class);
            aspectConfig.addBeforeWorker(new LoggingBeforeInvocation());
            aspectConfig.addAfterWorker(new LoggingAfterInvocation());
            MaterialCacheService materialCacheService = ContextUtil.context.getBean(MaterialCacheService.class);
            materialCacheService.init();
            JdbcTemplate jdbcTemplate = ContextUtil.getBean("jdbcTemplate");
            DBUtil.init(jdbcTemplate);
            logger.info("Spring is fully started, context-util is ready!");
        }, new ApplicationPidFileWriter());
        builder.bannerMode(Banner.Mode.OFF);
        builder.run(args);
        stopWatch.stop();
        logger.info("========= Service is fully started! ========= cost: " + stopWatch.getTotalTimeSeconds() + " seconds");
    }

}
