package com.sxjkwm.pm.business.message.service;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.message.dto.TextMsgDto;
import com.sxjkwm.pm.exception.PmException;

public interface MessageService {

    JSONObject send(TextMsgDto textMsgDto) throws PmException;

}
