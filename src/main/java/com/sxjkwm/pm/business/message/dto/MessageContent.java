package com.sxjkwm.pm.business.message.dto;

import java.io.Serializable;

public class MessageContent implements Serializable {
    /**
     * 销售内容
     */
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
