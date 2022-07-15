package com.sxjkwm.pm.common;

import java.util.concurrent.TimeUnit;

public interface CacheService {

    String getString(String key);

    void store(String key, String data);

    void remove(String key);

    void store(String key, String data, long expireTime, TimeUnit expireTimeUnit);

}
