package com.sxjkwm.pm.util;

import cn.hutool.crypto.digest.MD5;

/**
 * @author Vic.Chu
 * @date 2022/7/27 17:36
 */
public class PasswordUtil {

    public static String encryptPassword(String originalPassword) {
        return MD5.create().digestHex(originalPassword, "UTF-8");
    }

}
