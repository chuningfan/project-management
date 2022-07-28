package com.sxjkwm.pm.auth.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/7/27 17:21
 */
public class PageLoginFormDto implements Serializable {

    private String username;

    private String password;

    private boolean isAdmin;

    private String captcha;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
