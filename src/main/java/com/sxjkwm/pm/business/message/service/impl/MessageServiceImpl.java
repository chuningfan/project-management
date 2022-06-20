package com.sxjkwm.pm.business.message.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.message.dto.MessageContent;
import com.sxjkwm.pm.business.message.dto.TextMsgDto;
import com.sxjkwm.pm.business.message.service.MessageService;
import com.sxjkwm.pm.configuration.MessageConfig;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.HttpClientUtil;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageConfig messageConfig;


    @Override
    public JSONObject send(TextMsgDto textMsgDto) throws PmException {
        String token = WxWorkTokenUtil.getToken();
        String toUser = textMsgDto.getTouser().replace(",", "|");
        textMsgDto.setTouser(toUser);
        String content = textMsgDto.getText().getContent();
        int count = 0;
        while (count < 3) {
            String result = textMassage(textMsgDto, token);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (!Objects.isNull(jsonObject)) {
                Integer errCode = jsonObject.getInteger("errcode");
                if (errCode == 0) {
                    return jsonObject;
                } else {
                    textMsgDto.getText().setContent(content);
                    result = textMassage(textMsgDto, token);
                    count++;
                }
            } else {
                textMsgDto.getText().setContent(content);
                result = textMassage(textMsgDto, token);
                count++;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errcode", 500);
        jsonObject.put("errmsg", "推送失败");
        return jsonObject;
    }


    private String textMassage(TextMsgDto textMsgDto, String token) {
        MessageContent messageContent = new MessageContent();
        String content = textMsgDto.getText().getContent();
        messageContent.setContent(messageConfig.getMsgContent() + content);
        textMsgDto.setText(messageContent);
        // 推送消息
        String param = JSON.toJSONString(textMsgDto);
        String Url = messageConfig.getWXMessageURL() + token;
        String result = HttpClientUtil.doPostJson(Url, param);
        return result;
    }


}
