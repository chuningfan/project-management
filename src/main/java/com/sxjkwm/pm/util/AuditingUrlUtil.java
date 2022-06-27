package com.sxjkwm.pm.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.configuration.FEConfig;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/6/27 10:06
 */
public class AuditingUrlUtil {

    private static final String auditingUrlPattern = "%s/auditingRecord/%d?makata=%s";

    private static FEConfig feConfig = null;

    static {
        feConfig = ContextUtil.getBean(FEConfig.class);
    }

    public static String generateUrl(String auditorUserId, Long auditingRecordId) {
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("userId", auditorUserId);
        String jsonString = JSONObject.toJSONString(dataMap);
        String token = new String(Base64.getEncoder().encode(jsonString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return String.format(auditingUrlPattern, feConfig.getDomain(), auditingRecordId, token);
    }

}
