package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;

class ContextImpl implements Context<ContextFactoryImpl.AuthUser> {

    private ContextFactoryImpl.AuthUser authUser;

    private void setAuthUser(ContextFactoryImpl.AuthUser authUser) {
        this.authUser = authUser;
    }
    private ContextFactoryImpl.AuthUser getAuthUser() {
        return this.authUser;
    }

    @Override
    public Context<ContextFactoryImpl.AuthUser> of(ContextFactoryImpl.AuthUser authUser) {
        setAuthUser(authUser);
        return this;
    }

    @Override
    public ContextFactoryImpl.AuthUser unwrap() {
        return getAuthUser();
    }

}
