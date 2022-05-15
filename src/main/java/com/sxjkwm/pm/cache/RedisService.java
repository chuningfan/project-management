package com.sxjkwm.pm.cache;

import com.sxjkwm.pm.common.CacheService;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vic.Chu
 * @date 2022/5/13 21:04
 */
//@Service
public class RedisService implements CacheService {

    private final RedissonClient redissonClient;

    private final RMap<String, String> rMap;

    @Autowired
    public RedisService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
        rMap = redissonClient.getMap("sxjkwm_authUser");
    }

    @Override
    public String getString(String key) {
        return rMap.get(key);
    }

    @Override
    public void store(String key, String data) {
        rMap.put(key, data);
    }

    @Override
    public void remove(String key) {
        rMap.remove(key);
    }

}
