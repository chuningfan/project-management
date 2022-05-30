package com.sxjkwm.pm.util;

/**
 * @author Vic.Chu
 * @date 2022/5/27 12:53
 */
public class OSSUtil {

    public static String getBucketName(Long projectId) {
        if (projectId % 2 == 0) {
            return "evenbucket";
        } else {
            return "oddbucket";
        }
    }

}
