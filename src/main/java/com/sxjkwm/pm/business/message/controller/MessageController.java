package com.sxjkwm.pm.business.message.controller;

import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.business.message.dto.MessageContent;
import com.sxjkwm.pm.business.message.dto.MessageDto;
import com.sxjkwm.pm.business.message.service.MessageService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public RestResponse<JSONObject> send(@RequestBody MessageDto messageDto) throws PmException {
        JSONObject message = messageService.pushMessage(messageDto);
        return RestResponse.of(message);
    }


}
