package com.sxjkwm.pm.business.message.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.message.dto.MessageContent;
import com.sxjkwm.pm.business.message.dto.MessageDto;
import com.sxjkwm.pm.business.message.service.MessageService;
import com.sxjkwm.pm.configuration.MessageConfig;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.HttpClientUtil;
import com.sxjkwm.pm.util.WxWorkTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageConfig messageConfig;

    private static String token = null;

    static {
        try {
            token = WxWorkTokenUtil.getToken();
        } catch (PmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject pushMessage(MessageDto messageDto) {
        String toUser = messageDto.getTouser().replace(",", "|");
        messageDto.setTouser(toUser);
        messageDto.getText().getContent();
        MessageContent content = new MessageContent();
        content.setContent(messageConfig.getMsgContent() + messageDto.getText().getContent());
        messageDto.setText(content);
        // 推送消息
        String param = JSON.toJSONString(messageDto);
        String Url = messageConfig.getWXMessageURL() + token;
        String result = HttpClientUtil.doPostJson(Url, param);
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

}
