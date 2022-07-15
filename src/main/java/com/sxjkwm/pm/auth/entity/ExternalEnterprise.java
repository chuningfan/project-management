package com.sxjkwm.pm.auth.entity;

import com.sxjkwm.pm.common.BaseEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/7/15 8:33
 */
@Entity
@Table(name = "external_enterprise", indexes = {@Index(name = "ee_app_key_secret", columnList = "app_key,app_secret")})
@Proxy(lazy = false)
public class ExternalEnterprise extends BaseEntity {

    @Column(name = "enterprise_name")
    private String enterpriseName;

    @Column(name = "app_key")
    private String appKey;

    @Column(name = "app_secret")
    private String appSecret;

    @Column(name = "deadline")
    private Long deadline;

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }
}
