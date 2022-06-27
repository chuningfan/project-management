package com.sxjkwm.pm.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;

import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/6/17 14:15
 */
public class WxWorkMessageUtil {

    private static final String msgUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    public static void sendWxWorkMsg(String msg, String userId) throws PmException {
        String url = String.format(msgUrl, WxWorkTokenUtil.getToken());
        JSONObject param = new JSONObject();
        param.put("touser", userId);
        param.put("msgtype", "text");
        param.put("agentid", WxWorkTokenUtil.agentId);
        param.put("safe", 0);
        param.put("enable_id_trans", 0);
        param.put("enable_duplicate_check", 0);
        param.put("duplicate_check_interval", 1800);
        JSONObject content = new JSONObject();
        content.put("content", msg);
        param.put("text", content);
        String res = HttpUtil.post(url, param.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(res);
        Integer errorCode = resultJson.getInteger("errcode");
        if (Objects.nonNull(errorCode) && 0 != errorCode.intValue()) {
            throw new PmException(PmError.WXWORK_SEND_MSG_FAILED, resultJson.getString("errmsg"));
        }
    }

}
