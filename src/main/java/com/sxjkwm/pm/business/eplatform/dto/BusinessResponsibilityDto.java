package com.sxjkwm.pm.business.eplatform.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/8/23 16:37
 */
public class BusinessResponsibilityDto implements Serializable {

    private Long id;

    private String ownerId;

    private String buyerOrgs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getBuyerOrgs() {
        return buyerOrgs;
    }

    public void setBuyerOrgs(String buyerOrgs) {
        this.buyerOrgs = buyerOrgs;
    }
}
