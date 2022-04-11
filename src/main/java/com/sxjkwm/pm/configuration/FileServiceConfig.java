package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.business.file.service.ProjectFileService;
import com.sxjkwm.pm.business.file.service.impl.DefaultProjectFileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileServiceConfig {

    @Bean
    @ConditionalOnMissingBean({ProjectFileService.class})
    public DefaultProjectFileServiceImpl defaultProjectFileService() {
        return new DefaultProjectFileServiceImpl();
    }

}
