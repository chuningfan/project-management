package com.sxjkwm.pm.thirdparty.do1.token.impl;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.cache.RedisService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.thirdparty.do1.token.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vic.Chu
 * @date 2022/9/15 15:55
 */
@Component
public class Do1TokenHandler extends TokenHandler {

    private RedisService redisService;

    @Autowired
    public Do1TokenHandler(RedisService redisService) throws PmException {
        super("do1",
                "qwef9feca6beed4e618b59d3696e766e75",
                "YzAxYTY0NzAtOTQ3Zi00YTMwLTg2NTQtY2Q0YWFhZjAwYjdh",
                "https://qwif.do1.com.cn/qwcgi/portal/api/qwsecurity!getToken.action?developerId={key}&developerKey={secret}",
                null, null, redisService);
    }

    @Override
    protected TokenInfo checkAndExtractToken(JSONObject jsonObject) throws Throwable {
        if (Objects.isNull(jsonObject)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        if ("0".equals(jsonObject.getString("code"))) {
            jsonObject = jsonObject.getJSONObject("data");
            TokenInfo tokenInfo = new TokenInfo(jsonObject.getString("token"), jsonObject.getLong("expiresIn"), TimeUnit.SECONDS);
            return tokenInfo;
        }
        return null;
    }
}
