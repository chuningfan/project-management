package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("openApiContextFactory")
public class OpenApiContextFactoryImpl implements ContextFactory<ExternalRPCDataDto> {
    static final ThreadLocal<Context<ExternalRPCDataDto>> contextThreadLocal = new ThreadLocal<>();
    @Override
    public Context<ExternalRPCDataDto> get() {
        Context<ExternalRPCDataDto> context = contextThreadLocal.get();
        if (Objects.isNull(context)) {
            context = new OpenApiContextImpl();
            contextThreadLocal.set(context);
        }
        return context;
    }

    @Override
    public void remove() {
        contextThreadLocal.remove();
    }

}
