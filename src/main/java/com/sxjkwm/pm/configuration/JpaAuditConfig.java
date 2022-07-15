package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.context.impl.ContextFactoryImpl;
import com.sxjkwm.pm.auth.context.impl.OpenApiContextFactoryImpl;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.auth.dto.UserDataDto;
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

    private final ContextFactoryImpl userContextFactory;

    private final OpenApiContextFactoryImpl openApiContextFactory;

    @Autowired
    public JpaAuditConfig(ContextFactoryImpl userContextFactory, OpenApiContextFactoryImpl openApiContextFactory) {
        this.userContextFactory = userContextFactory;
        this.openApiContextFactory = openApiContextFactory;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            String operator = "system";
            Context<UserDataDto> context = userContextFactory.get();
            UserDataDto authUser = context.unwrap();
            if (Objects.nonNull(authUser)) {
                operator = authUser.getWxUserId();
            } else {
                Context<ExternalRPCDataDto> rpcContext = openApiContextFactory.get();
                ExternalRPCDataDto rpcDataDto = rpcContext.unwrap();
                if (Objects.nonNull(rpcDataDto)) {
                    operator = rpcDataDto.getAppKey();
                }
            }
            return Optional.ofNullable(operator);
        };
    }

}
