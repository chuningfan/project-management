package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.common.AuthUser;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContextFactoryImpl implements ContextFactory<UserDataDto> {
    private static final ThreadLocal<Context<UserDataDto>> contextThreadLocal = new ThreadLocal<>();
    @Override
    public Context<UserDataDto> get() {
        Context<UserDataDto> context = contextThreadLocal.get();
        if (Objects.isNull(context)) {
            context = new ContextImpl();
            contextThreadLocal.set(context);
        }
        return context;
    }

    @Override
    public void remove() {
        contextThreadLocal.remove();
    }

}
