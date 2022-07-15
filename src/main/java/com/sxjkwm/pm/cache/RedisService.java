package com.sxjkwm.pm.cache;

import com.sxjkwm.pm.common.CacheService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vic.Chu
 * @date 2022/5/13 21:04
 */
@Service
public class RedisService implements CacheService {

    private static final long expireTime = 3600 * 5;

    private static final TimeUnit expireTimeUnit = TimeUnit.SECONDS;

    private final RedissonClient redissonClient;

    @Autowired
    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String getString(String key) {
        Object obj = redissonClient.getBucket(key).get();
        return Objects.isNull(obj) ? null : obj.toString();
    }

    @Override
    public void store(String key, String data) {
        redissonClient.getBucket(key).set(data, expireTime, expireTimeUnit);
    }

    @Override
    public void remove(String key) {
        redissonClient.getBucket(key).delete();
    }

    @Override
    public void store(String key, String data, long expireTime, TimeUnit expireTimeUnit) {
        redissonClient.getBucket(key).set(data, expireTime, expireTimeUnit);
    }

}
