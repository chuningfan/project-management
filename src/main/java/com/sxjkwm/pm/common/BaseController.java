package com.sxjkwm.pm.common;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.service.UserDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class BaseController {

    @Autowired
    private ContextFactory<AuthUser> contextFactory;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserDataService userDataService;

    private Context<AuthUser> getContext() {
        return contextFactory.get();
    }

    protected UserDataDto getUserData() {
        AuthUser currentUser = getContext().unwrap();
        if (Objects.isNull(currentUser)) {
            return null;
        }
        String userId = currentUser.getUserId();
        String userDataDtoJson = cacheService.getString(userId);
        if (StringUtils.isBlank(userDataDtoJson)) {
            UserDataDto userDataDto = userDataService.getUserDataByWxUserId(userId);
            if (Objects.nonNull(userDataDto)) {
                cacheService.store(userId, JSONObject.toJSONString(userDataDto));
            }
            return userDataDto;
        }
        return JSONObject.parseObject(userDataDtoJson, UserDataDto.class);
    }

    protected AuthUser getAuthUser() {
        return getContext().unwrap();
    }

}
