package com.sxjkwm.pm.business.message.dto;

import java.io.Serializable;

public class TextMsgDto extends MessageDto implements Serializable {
    /**
     * 消息类型
     */
    private String msgtype = "text";
    /**
     * 消息体
     */
    private MessageContent text;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public MessageContent getText() {
        return text;
    }

    public void setText(MessageContent text) {
        this.text = text;
    }
}
