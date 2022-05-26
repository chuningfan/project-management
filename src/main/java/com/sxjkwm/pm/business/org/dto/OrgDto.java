package com.sxjkwm.pm.business.org.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/24 18:20
 */
public class OrgDto implements Serializable {

    private Long id;

    private String orgName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

}
