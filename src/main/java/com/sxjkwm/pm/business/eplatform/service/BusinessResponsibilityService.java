package com.sxjkwm.pm.business.eplatform.service;

import com.google.common.base.Splitter;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.eplatform.dao.BusinessResponsibilityDao;
import com.sxjkwm.pm.business.eplatform.dto.BusinessResponsibilityDto;
import com.sxjkwm.pm.business.eplatform.entity.BusinessResponsibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/8/23 16:36
 */
@Service
public class BusinessResponsibilityService {

    private final BusinessResponsibilityDao businessResponsibilityDao;

    @Autowired
    public BusinessResponsibilityService(BusinessResponsibilityDao businessResponsibilityDao) {
        this.businessResponsibilityDao = businessResponsibilityDao;
    }

    public Long saveOrUpdate(BusinessResponsibilityDto dto) {
        String ownerId = dto.getOwnerId();
        BusinessResponsibility businessResponsibility = businessResponsibilityDao.findByOwnerId(ownerId);
        if (Objects.isNull(businessResponsibility)) {
            businessResponsibility = new BusinessResponsibility();
            businessResponsibility.setOwnerId(ownerId);
        }
        businessResponsibility.setBuyerOrgs(dto.getBuyerOrgs());
        businessResponsibility = businessResponsibilityDao.save(businessResponsibility);
        return businessResponsibility.getId();
    }

    public List<String> get() {
        String ownerId = ContextHelper.getUserData().getWxUserId();
        BusinessResponsibility businessResponsibility = businessResponsibilityDao.findByOwnerId(ownerId);
        if (Objects.isNull(businessResponsibility)) {
            return Collections.emptyList();
        }
        String orgsStr = businessResponsibility.getBuyerOrgs();
        return Splitter.on(",").splitToList(orgsStr);
    }

}
