package com.sxjkwm.pm.common;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class BaseController {

    @Autowired
    private ContextFactory<UserDataDto> contextFactory;

    private Context<UserDataDto> getContext() {
        return contextFactory.get();
    }

    protected UserDataDto getUserData() {
        UserDataDto currentUser = getContext().unwrap();
        if (Objects.isNull(currentUser)) {
            return null;
        }
        return currentUser;
    }

    protected UserDataDto getAuthUser() {
        return getContext().unwrap();
    }

}
