package com.sxjkwm.pm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String username = "system";
//            SecurityContext context = SecurityContextHolder.getContext();
//            if (context != null) {
//                Authentication authentication = context.getAuthentication();
//                if (authentication != null) {
//                    username = authentication.getName();
//                }
//            }
            String result = username;
            return Optional.ofNullable(result);
        };
    }

}
