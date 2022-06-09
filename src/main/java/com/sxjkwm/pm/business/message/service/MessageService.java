package com.sxjkwm.pm.business.message.service;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.message.dto.MessageDto;
import com.sxjkwm.pm.exception.PmException;

public interface MessageService {



    JSONObject pushMessage(MessageDto messageDto) throws PmException;

}
