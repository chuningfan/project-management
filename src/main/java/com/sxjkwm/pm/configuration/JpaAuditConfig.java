package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.context.impl.ContextFactoryImpl;
import com.sxjkwm.pm.common.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {

    private final ContextFactory<AuthUser> contextFactory;

    @Autowired
    public JpaAuditConfig(ContextFactory<AuthUser> contextFactory) {
        this.contextFactory = contextFactory;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String operator = "system";
            Context<AuthUser> context = contextFactory.get();
            AuthUser authUser = context.unwrap();
            if (Objects.nonNull(authUser)) {
                operator = authUser.getUserId();
            }
            return Optional.ofNullable(operator);
        };
    }

}
