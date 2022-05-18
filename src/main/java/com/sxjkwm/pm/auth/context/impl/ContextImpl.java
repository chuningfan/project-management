package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.common.AuthUser;

class ContextImpl implements Context<UserDataDto> {

    private UserDataDto authUser;

    private void setAuthUser(UserDataDto authUser) {
        this.authUser = authUser;
    }
    private UserDataDto getAuthUser() {
        return this.authUser;
    }

    @Override
    public Context<UserDataDto> of(UserDataDto authUser) {
        setAuthUser(authUser);
        return this;
    }

    @Override
    public UserDataDto unwrap() {
        return getAuthUser();
    }

}
