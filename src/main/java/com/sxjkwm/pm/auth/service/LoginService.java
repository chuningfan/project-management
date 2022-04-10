package com.sxjkwm.pm.auth.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.configuration.WxConfig;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginService {

    public static final String tokenKey = "makata";

    private final WxConfig wxConfig;

    @Autowired
    public LoginService(WxConfig wxConfig) {
        this.wxConfig = wxConfig;
    }

    public boolean isValid(HttpServletRequest req) {
        String token = req.getHeader(tokenKey);
        if (StringUtils.isBlank(token)) {
            return false;
        }
        JSONObject jsonObject = JSONObject.parseObject(new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
        String userId = jsonObject.getString("userId");
        return StringUtils.isNotBlank(userId);
    }

    public JSONObject doLogin(HttpServletRequest req) throws PmException {
        String code = req.getParameter("code");
        String token = WxWorkTokenUtil.getToken();
        Map<String, Object> param = Maps.newHashMap();
        param.put("access_token", token);
        param.put("code", code);
        String res = HttpUtil.get(wxConfig.getLoginCodeURL(), param, 30 * 1000);
        JSONObject jsonObject = JSONObject.parseObject(res);
        Integer errCode = jsonObject.getInteger("errcode");
        if (Objects.isNull(errCode) || errCode.intValue() == 0) {
            String userId = jsonObject.getString("UserId");
            param.put("userid", userId);
            param.remove("code");
            res = HttpUtil.get(wxConfig.getUserURL(), param, 30 * 1000);
            jsonObject = JSONObject.parseObject(res);
            errCode = jsonObject.getInteger("errcode");
            if (Objects.isNull(errCode) || errCode.intValue() == 0) {
                return jsonObject;
            }
            throw new PmException(PmError.WXWORK_READ_USER_FAILED);
        }
        throw new PmException(PmError.WXWORK_LOGIN_FAILED);
    }

    public String processToken(JSONObject jsonObject, HttpServletRequest request) {
        String username = jsonObject.getString("name");
        List<Integer> deptIds = (List<Integer>) jsonObject.get("department");
        String avatar = jsonObject.getString("avatar");
        String mobile = jsonObject.getString("mobile");
        String userId = jsonObject.getString("userid");
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("username", username);
        dataMap.put("deptIds", deptIds);
        dataMap.put("avatar", avatar);
        dataMap.put("mobile", mobile);
        dataMap.put("userId", userId);
        String jsonString = JSONObject.toJSONString(dataMap);
        String encoded = new String(Base64.getEncoder().encode(jsonString.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        request.getSession().setAttribute(tokenKey, encoded);
        return encoded;
    }

}
