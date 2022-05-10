package com.sxjkwm.pm.cache;

import com.google.common.collect.Maps;
import com.sxjkwm.pm.common.CacheService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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
}
