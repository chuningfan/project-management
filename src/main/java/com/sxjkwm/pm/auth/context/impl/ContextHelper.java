package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.auth.dto.UserDataDto;

import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/5/27 20:09
 */
public class ContextHelper {

    public static UserDataDto getUserData() {
        Context<UserDataDto> context = ContextFactoryImpl.contextThreadLocal.get();
        if (Objects.isNull(context)) {
            return null;
        }
        return context.unwrap();
    }

    public static ExternalRPCDataDto getExternalRpcData() {
        Context<ExternalRPCDataDto> context = OpenApiContextFactoryImpl.contextThreadLocal.get();
        if (Objects.isNull(context)) {
            return null;
        }
        return context.unwrap();
    }

}
