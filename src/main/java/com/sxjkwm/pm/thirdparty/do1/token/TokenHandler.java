package com.sxjkwm.pm.thirdparty.do1.token;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.cache.RedisService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vic.Chu
 * @date 2022/9/15 14:11
 */
public abstract class TokenHandler {

    private static final String cachePrefix = "third_party_api_token:";

    private static final String cacheLockPrefix = "lock_third_party_api_token:";

    private static final String apiKeyReplKey = "{key}";

    private static final String apiSecretReplKey = "{secret}";

    protected String thirdParty;

    protected String key;

    protected String secret;

    protected String url;

    protected Long tokenTimeout;

    protected TimeUnit tokenTimeoutUnit;

    private RedisService redisService;

    private static String localToken = null;

    private static Long lastUpdateTime = -1L;

    public TokenHandler(@NonNull String thirdParty, @NonNull String key, @NonNull String secret, @NonNull String url, Long tokenTimeout, TimeUnit tokenTimeoutUnit, RedisService redisService) throws PmException {
        this.thirdParty = thirdParty;
        this.key = key;
        this.secret = secret;
        this.url = url;
        this.tokenTimeout = tokenTimeout;
        this.tokenTimeoutUnit = tokenTimeoutUnit;
        this.redisService = redisService;
        checkConfig();
    }

    private void checkConfig() throws PmException {
        if (!url.contains(apiKeyReplKey) || !url.contains(apiSecretReplKey)) {
            throw new PmException("Token URL should contains {key} & {secret}");
        }
    }

    public String token() throws Throwable {
        if (Objects.isNull(localToken)) { // need to reload token
            String tokenString = redisService.getString(getTokenCacheKey());
            if (StringUtils.isNotBlank(tokenString)) {
                Token token = JSONObject.parseObject(tokenString, Token.class);
                localToken = token.getToken();
                lastUpdateTime = token.getUpdateTime();
            } else {
                RLock lock = redisService.getLock(getTokenLockCacheKey());
                if (lock.tryLock(3500, TimeUnit.MILLISECONDS)) {
                    try {
                        String getUrl = url.replace(apiKeyReplKey, key).replace(apiSecretReplKey, secret);
                        String jsonString = HttpUtil.get(getUrl);
                        TokenInfo tokenInfo = checkAndExtractToken(JSONObject.parseObject(jsonString));
                        if (Objects.nonNull(tokenInfo)) {
                            localToken = tokenInfo.getToken();
                            lastUpdateTime = System.currentTimeMillis();
                            Token token = new Token(localToken, lastUpdateTime);
                            if (Objects.isNull(tokenTimeout)) {
                                tokenTimeout = tokenInfo.getTimeout();
                                tokenTimeoutUnit = tokenInfo.getTimeUnit();
                            }
                            redisService.store(getTokenCacheKey(), JSONObject.toJSONString(token), tokenTimeout, tokenTimeoutUnit);
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {
                    Thread.sleep(3500);
                    tokenString = redisService.getString(getTokenCacheKey());
                    if (StringUtils.isBlank(tokenString)) {
                        throw new PmException(PmError.TRY_AG_LATER);
                    }
                    Token token = JSONObject.parseObject(tokenString, Token.class);
                    localToken = token.getToken();
                    lastUpdateTime = token.getUpdateTime();
                }
            }
        } else {
            if (lastUpdateTime.intValue() == -1 || (lastUpdateTime + tokenTimeoutUnit.toMillis(tokenTimeout) > System.currentTimeMillis())) {
                localToken = null;
                return token();
            }
        }
        return localToken;

    }

    private String getTokenCacheKey() {
        return cachePrefix + this.thirdParty;
    }

    private String getTokenLockCacheKey() {
        return cacheLockPrefix + this.thirdParty;
    }

    protected abstract TokenInfo checkAndExtractToken(JSONObject jsonObject) throws Throwable;

    protected static class Token implements Serializable {
        private String token;
        private Long updateTime;

        public Token(String token, Long updateTime) {
            this.token = token;
            this.updateTime = updateTime;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }

    protected static class TokenInfo implements Serializable {
        private String token;

        private Long timeout;

        private TimeUnit timeUnit;

        public TokenInfo(String token, Long timeout, TimeUnit timeUnit) {
            this.token = token;
            this.timeout = timeout;
            this.timeUnit = timeUnit;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Long getTimeout() {
            return timeout;
        }

        public void setTimeout(Long timeout) {
            this.timeout = timeout;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }

}
