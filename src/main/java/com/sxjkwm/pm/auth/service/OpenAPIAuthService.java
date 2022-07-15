package com.sxjkwm.pm.auth.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.dao.ExternalEnterpriseDao;
import com.sxjkwm.pm.auth.dto.ExternalRPCDataDto;
import com.sxjkwm.pm.auth.entity.ExternalEnterprise;
import com.sxjkwm.pm.common.CacheService;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vic.Chu
 * @date 2022/7/15 8:29
 */
@Service
public class OpenAPIAuthService {

    private static final String headerKeySign = "sign";

    private static final String headerKeyAppKey = "appKey";

    private static final String headerKeyAppSecret = "appSecret";

    private static final String salt = "sxjkwm2808";

    private static final String[] authKeys = {headerKeySign, headerKeyAppKey};

    private final ExternalEnterpriseDao externalEnterpriseDao;

    private final CacheService cacheService;

    private final ContextFactory openApiContextFactory;

    @Autowired
    public OpenAPIAuthService(ExternalEnterpriseDao externalEnterpriseDao, CacheService cacheService, @Qualifier("openApiContextFactory") ContextFactory openApiContextFactory) {
        this.externalEnterpriseDao = externalEnterpriseDao;
        this.cacheService = cacheService;
        this.openApiContextFactory = openApiContextFactory;
    }

    public Map<String, String> generateBaseInfo(String enterpriseName, Long deadline) throws PmException {
        ExternalEnterprise condition = new ExternalEnterprise();
        condition.setEnterpriseName(enterpriseName);
        ExternalEnterprise enterprise = externalEnterpriseDao.findOne(Example.of(condition)).orElse(null);
        if (Objects.nonNull(enterprise)) {
            throw new PmException(PmError.ENTERPRISE_NAME_IS_EXISTING);
        }
        enterprise = new ExternalEnterprise();
        enterprise.setEnterpriseName(enterpriseName);
        enterprise.setDeadline(deadline);
        String appKey = DigestUtils.md5Hex(salt + enterpriseName);
        enterprise.setAppKey(appKey);
        String appSecret = DigestUtils.md5Hex(appKey + System.currentTimeMillis() + "_" + deadline);
        enterprise.setAppSecret(appSecret);
        externalEnterpriseDao.save(enterprise);
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put(headerKeyAppKey, enterprise.getAppKey());
        resultMap.put(headerKeyAppSecret, enterprise.getAppSecret());
        return resultMap;
    }



    public String getSign(String appKey, String appSecret) throws PmException {
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(appSecret)) {
            throw new PmException(PmError.INVALID_KEY_OR_SECRET);
        }
        ExternalEnterprise condition = new ExternalEnterprise();
        condition.setAppKey(appKey);
        condition.setAppSecret(appSecret);
        ExternalEnterprise externalEnterprise = externalEnterpriseDao.findOne(Example.of(condition)).orElse(null);
        if (Objects.isNull(externalEnterprise)) {
            throw new PmException(PmError.INVALID_KEY_OR_SECRET);
        }
        Long deadline = externalEnterprise.getDeadline();
        if (deadline - System.currentTimeMillis() <= 0) {
            throw new PmException(PmError.PERMISSION_IS_DENIED);
        }
        return generateSign(appKey, appSecret);
    }

    public void auth(HttpServletRequest request) throws PmException {
        Map<String, String> result = validate(request);
        String err = result.get("err");
        if (StringUtils.isNotBlank(err)) {
            throw new PmException(err + " cannot be null or empty");
        }
        String sign = result.get(headerKeySign);
        String appKey = result.get(headerKeyAppKey);
        String cachedSignKey = getCachedSignKey(appKey);
        String authedSign = cacheService.getString(cachedSignKey);
        if (StringUtils.isBlank(authedSign)) {
            throw new PmException(PmError.SIGN_EXPIRED);
        }
        if (!authedSign.equalsIgnoreCase(sign)) {
            throw new PmException(PmError.INVALID_SIGN);
        }
        fillContext(request, appKey);
    }

    private void fillContext(HttpServletRequest request, String appKey) {
        Context<ExternalRPCDataDto> context = openApiContextFactory.get();
        ExternalRPCDataDto dataDto = new ExternalRPCDataDto();
        dataDto.setAppKey(appKey);
        dataDto.setReqIp(request.getRemoteAddr());
        context.of(dataDto);
    }

    private Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> map = Maps.newHashMap();
        List<String> missingKeys = Lists.newArrayList();
        for (String key: authKeys) {
            String val = request.getHeader(key);
            if (StringUtils.isBlank(val)) {
                missingKeys.add(key);
                continue;
            }
            map.put(key, val);
        }
        if (CollectionUtils.isNotEmpty(missingKeys)) {
            map.put("err", Joiner.on(",").join(missingKeys));
        }
        return map;
    }

    private String generateSign(String appKey, String appSecret) {
        String cachedKey = getCachedSignKey(appKey);
        String sign = cacheService.getString(cachedKey);
        if (StringUtils.isBlank(sign)) {
            String origin = "" + salt.charAt(0) + salt.charAt(1) + appKey + appSecret + System.currentTimeMillis();
            sign = DigestUtils.md5Hex(origin);
            cacheService.store(cachedKey, sign, 1800, TimeUnit.SECONDS);
        }
        return sign;
    }

    private String getCachedSignKey(String appKey) {
        return Constant.openAPISignCachePrefix + DigestUtils.md5Hex(appKey);
    }

}
