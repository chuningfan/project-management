package com.sxjkwm.pm.auth.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/7/15 14:55
 */
public class ExternalEnterpriseRegisterDto implements Serializable {

    private String enterpriseName;

    private Long deadline;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }
}
