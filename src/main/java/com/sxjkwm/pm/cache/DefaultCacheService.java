package com.sxjkwm.pm.cache;

import com.google.common.collect.Maps;
import com.sxjkwm.pm.common.CacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnMissingBean(RedisService.class)
public class DefaultCacheService implements CacheService {

    private static final Map<String, String> userDataCache = Maps.newHashMap();

    @Override
    public String getString(String key) {
        return userDataCache.get(key);
    }

    @Override
    public void store(String key, String data) {
        userDataCache.put(key, data);
    }

    @Override
    public void remove(String key) {
        userDataCache.remove(key);
    }

    @Override
    public void store(String key, String data, long expireTime, TimeUnit expireTimeUnit) {
        throw new UnsupportedOperationException();
    }
}
