package com.sxjkwm.pm.auditing.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:46
 */
public class AuditingFlowDto implements Serializable {

    private Long id;

    private String name;

    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
