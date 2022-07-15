package com.sxjkwm.pm.auth.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/7/15 8:31
 */
public class ExternalRPCDataDto implements Serializable {

    private String appKey;

    private String reqIp;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getReqIp() {
        return reqIp;
    }

    public void setReqIp(String reqIp) {
        this.reqIp = reqIp;
    }
}
