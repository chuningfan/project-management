package com.sxjkwm.pm.auth.context.impl;

import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.auth.dto.UserDataDto;

/**
 * @author Vic.Chu
 * @date 2022/7/15 14:02
 */
public class OpenApiContextImpl implements Context<ExternalRPCDataDto> {

    private ExternalRPCDataDto externalRPCDataDto;

    public ExternalRPCDataDto getExternalRPCDataDto() {
        return externalRPCDataDto;
    }

    public void setExternalRPCDataDto(ExternalRPCDataDto externalRPCDataDto) {
        this.externalRPCDataDto = externalRPCDataDto;
    }

    @Override
    public Context<ExternalRPCDataDto> of(ExternalRPCDataDto externalRPCDataDto) {
        setExternalRPCDataDto(externalRPCDataDto);
        return this;
    }

    @Override
    public ExternalRPCDataDto unwrap() {
        return getExternalRPCDataDto();
    }
}
