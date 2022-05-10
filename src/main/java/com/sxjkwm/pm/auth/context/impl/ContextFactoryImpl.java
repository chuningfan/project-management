package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.common.AuthUser;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ContextFactoryImpl implements ContextFactory<AuthUser> {
    private static final ThreadLocal<Context<AuthUser>> contextThreadLocal = new ThreadLocal<>();
    @Override
    public Context<AuthUser> get() {
        Context<AuthUser> context = contextThreadLocal.get();
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
