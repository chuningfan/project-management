package com.sxjkwm.pm.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;

public class WxWorkTokenUtil {

    public static final String agentId = "1000009";

    public static final String corpId = "ww10a05925daeee4ca";

    private static final String secret = "NGEQ1N0jYY3SMSVFEgflZdnK5d4ylS_GwXHFvFpPa4g";

    private static final String tokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + secret;

    private static volatile long lastModifiedTime = 0;

    private static final long threshold = 7000;

    private static final String noneToken = "nil";

    private static volatile String latestToken = noneToken;

    public static String getToken() throws PmException {
        String token = null;
        if (lastModifiedTime > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastModifiedTime >= threshold - 200) {
                if (!noneToken.equals(latestToken)) {
                    return latestToken;
                }
            }
        }
        synchronized (latestToken) {
            if (noneToken.equals(latestToken)) {
                token = HttpUtil.get(tokenUrl);
                lastModifiedTime = System.currentTimeMillis();
                JSONObject jsonObject = JSONObject.parseObject(token);
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode.intValue() == 0) {
                    latestToken = jsonObject.getString("access_token");
                } else {
                    throw new PmException(PmError.WXWORK_TOKEN_ERROR, jsonObject.getString("errmsg"));
                }
            }
        }
        return latestToken;
    }

}
