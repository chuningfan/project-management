package com.sxjkwm.pm.auditing.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/7 19:04
 */
public class AuditorGroupDto implements Serializable {

    private Long id;

    private List<String> userIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

}
