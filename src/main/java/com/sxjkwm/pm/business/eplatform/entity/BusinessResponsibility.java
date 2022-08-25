package com.sxjkwm.pm.business.eplatform.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/8/23 16:28
 */
@Entity
@Table(name = "ep_business_responsibility", indexes = {@Index(name = "ep_biz_resp_ownerid", columnList = "owner_id")})
public class BusinessResponsibility extends BaseEntity {

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "buyer_orgs", columnDefinition = "TEXT")
    private String buyerOrgs;

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
