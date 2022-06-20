package com.sxjkwm.pm.business.message.dto;

import java.io.Serializable;

public class MessageDto implements Serializable {

    /**
     * 接收消息成员
     */
    private String touser;
    /**
     * 接收消息部门
     */
    private String toparty;
    /**
     * 接收消息标签
     */
    private String totag;
    /**
     * 企业应用ID
     */
    private Integer agentid = 1000009;


    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getToparty() {
        return toparty;
    }

    public void setToparty(String toparty) {
        this.toparty = toparty;
    }

    public String getTotag() {
        return totag;
    }

    public void setTotag(String totag) {
        this.totag = totag;
    }


    public Integer getAgentid() {
        return agentid;
    }

    public void setAgentid(Integer agentid) {
        this.agentid = agentid;
    }


}
