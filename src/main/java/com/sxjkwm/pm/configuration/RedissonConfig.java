package com.sxjkwm.pm.configuration;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Vic.Chu
 * @date 2022/5/13 20:51
 */
@Configuration
public class RedissonConfig {

    public RedissonClient redissonClient(@Autowired RedisConfig redisConfig) {
        Config config = new Config();
        config.setCodec(new StringCodec());
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redisConfig.getAddress())
                .setTimeout(redisConfig.getTimeout())
                .setConnectionPoolSize(redisConfig.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redisConfig.getConnectionMinimumIdleSize());

        if(StringUtils.isNotEmpty(redisConfig.getPassword())) {
            serverConfig.setPassword(redisConfig.getPassword());
        }
        return Redisson.create(config);
    }

}
