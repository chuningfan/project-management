package com.sxjkwm.pm.auth.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auth.context.Context;
import com.sxjkwm.pm.auth.context.ContextFactory;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.common.CacheService;
import com.sxjkwm.pm.configuration.WxConfig;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.IpAddressUtil;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginService {

    public static final String keyArchKey = "dejavu";

    public static final String tokenKey = "makata";

    public static final String wxErrorKey = "errcode";

    private static final String wxReturnCodeKey = "code";

    private final WxConfig wxConfig;

    private final ContextFactory<UserDataDto> contextFactory;

    private final CacheService cacheService;

    private final UserDataService userDataService;

    @Autowired
    public LoginService(WxConfig wxConfig, ContextFactory<UserDataDto> contextFactory, CacheService cacheService, UserDataService userDataService) {
        this.wxConfig = wxConfig;
        this.contextFactory = contextFactory;
        this.cacheService = cacheService;
        this.userDataService = userDataService;
    }

    public Boolean logout() {
        try {
            UserDataDto userDataDto = ContextHelper.getUserData();
            String userId = userDataDto.getWxUserId();
            cacheService.remove(userId);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    public boolean isValid(HttpServletRequest req) throws UnsupportedEncodingException {
        String token = req.getHeader(tokenKey);
        if (StringUtils.isBlank(token)) {
            token = req.getParameter(tokenKey);
            if (StringUtils.isBlank(token)) {
                return false;
            }
        }
        String _token = token;
        if (token.indexOf("%") > -1) {
            _token = token.replace(token.substring(token.lastIndexOf("%")), "=");
        }
        JSONObject jsonObject = JSONObject.parseObject(new String(Base64.getDecoder().decode(_token.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        String userId = jsonObject.getString(keyArchKey);
        String userDataDtoString = cacheService.getString(Constant.userCachePrefix + userId);

        if (StringUtils.isNotBlank(userDataDtoString)) {
            fillContext(JSONObject.parseObject(userDataDtoString, UserDataDto.class), req);
            return true;
        }
        return false;
    }

    private void fillContext(UserDataDto userDataDto, HttpServletRequest req) {
        String remoteIpAddr = IpAddressUtil.getIpAddress(req);
        userDataDto.setIpAddr(remoteIpAddr);
        Context<UserDataDto> context = contextFactory.get();
        context.of(userDataDto);
    }

    public JSONObject doLogin(HttpServletRequest req) throws PmException {
        String code = req.getParameter(wxReturnCodeKey);
        String token = WxWorkTokenUtil.getToken();
        Map<String, Object> param = Maps.newHashMap();
        param.put("access_token", token);
        param.put(wxReturnCodeKey, code);
        String res = HttpUtil.get(wxConfig.getLoginCodeURL(), param, 30 * 1000);
        JSONObject jsonObject = JSONObject.parseObject(res);
        Integer errCode = jsonObject.getInteger(wxErrorKey);
        if (Objects.isNull(errCode) || errCode.intValue() == 0) {
            String wxUserId = jsonObject.getString("UserId");
            param.put("userid", wxUserId);
            param.remove(wxReturnCodeKey);
            res = HttpUtil.get(wxConfig.getUserURL(), param, 30 * 1000);
            jsonObject = JSONObject.parseObject(res);
            errCode = jsonObject.getInteger(wxErrorKey);
            if (Objects.isNull(errCode) || errCode.intValue() == 0) {
                return jsonObject;
            }
            throw new PmException(PmError.WXWORK_READ_USER_FAILED);
        }
        throw new PmException(PmError.WXWORK_LOGIN_FAILED);
    }

    public String processToken(JSONObject jsonObject) {
        String wxUserId = jsonObject.getString("userid");
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put(keyArchKey, wxUserId);
        dataMap.put("time", System.currentTimeMillis());
        String jsonString = JSONObject.toJSONString(dataMap);
        String encoded = new String(Base64.getEncoder().encode(jsonString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        UserDataDto dataDto = userDataService.getUserDataByWxUserId(wxUserId);
        if (Objects.isNull(dataDto)) {
            return null;
        }
        String userDataString = JSONObject.toJSONString(dataDto);
        cacheService.store(Constant.userCachePrefix + wxUserId, userDataString);
        return encoded;
    }

}
