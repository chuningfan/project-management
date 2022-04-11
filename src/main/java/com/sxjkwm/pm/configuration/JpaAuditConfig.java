package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.context.impl.ContextFactoryImpl;
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

    private final ContextFactory<ContextFactoryImpl.AuthUser> contextFactory;

    @Autowired
    public JpaAuditConfig(ContextFactory<ContextFactoryImpl.AuthUser> contextFactory) {
        this.contextFactory = contextFactory;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String operator = "system";
            Context<ContextFactoryImpl.AuthUser> context = contextFactory.get();
            if (Objects.nonNull(context)) {
                ContextFactoryImpl.AuthUser authUser = context.unwrap();
                operator = authUser.getUserId();
            }
            return Optional.ofNullable(operator);
        };
    }

}
