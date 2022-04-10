package com.sxjkwm.pm.common;

public interface CacheService {

    String getString(String key);

    void store(String key, String data);

    void remove(String key);

}
