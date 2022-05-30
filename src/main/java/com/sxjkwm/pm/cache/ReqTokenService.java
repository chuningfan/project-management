package com.sxjkwm.pm.cache;

import cn.hutool.core.lang.UUID;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vic.Chu
 * @date 2022/5/30 8:42
 */
@Service
public class ReqTokenService {

    private final String reqTokenKey = "reqToken";

    private final RedissonClient redissonClient;

    @Autowired
    public ReqTokenService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public String generateToken() {
        UserDataDto userDataDto = ContextHelper.getUserData();
        if (Objects.isNull(userDataDto)) {
            return null;
        }
        String userId = userDataDto.getWxUserId();
        String key = reqTokenKey + ":" + userId;
        String val = UUID.fastUUID().toString();
        redissonClient.getBucket(key).set(val, 300, TimeUnit.SECONDS);
        return val;
    }

    public String getToken() {
        UserDataDto userDataDto = ContextHelper.getUserData();
        if (Objects.isNull(userDataDto)) {
            return null;
        }
        String userId = userDataDto.getWxUserId();
        String key = reqTokenKey + ":" + userId;
        RBucket<String> tokenBucket = redissonClient.getBucket(key);
        String token = tokenBucket.get();
        return token;
    }

}
