package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.common.AuthUser;

class ContextImpl implements Context<AuthUser> {

    private AuthUser authUser;

    private void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }
    private AuthUser getAuthUser() {
        return this.authUser;
    }

    @Override
    public Context<AuthUser> of(AuthUser authUser) {
        setAuthUser(authUser);
        return this;
    }

    @Override
    public AuthUser unwrap() {
        return getAuthUser();
    }

}
