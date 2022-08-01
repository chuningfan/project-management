package com.sxjkwm.pm.auth.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.UserAndRoleRelationDao;
import com.sxjkwm.pm.auth.entity.UserAndRoleRelation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserAndRoleRelationService {

    private final UserAndRoleRelationDao userAndRoleRelationDao;

    @Autowired
    public UserAndRoleRelationService(UserAndRoleRelationDao userAndRoleRelationDao) {
        this.userAndRoleRelationDao = userAndRoleRelationDao;
    }

    @Transactional
    public Boolean populateRelations(String wxUserId, List<String> roleNames) {
        UserAndRoleRelation condition = new UserAndRoleRelation();
        condition.setWxUserId(wxUserId);
        Example<UserAndRoleRelation> example = Example.of(condition);
        List<UserAndRoleRelation> existingRelations = userAndRoleRelationDao.findAll(example);
        if (CollectionUtils.isNotEmpty(existingRelations)) {
            userAndRoleRelationDao.deleteInBatch(existingRelations);
        }
        if (CollectionUtils.isNotEmpty(roleNames)) {
            List<UserAndRoleRelation> relations = Lists.newArrayList();
            UserAndRoleRelation relation;
            for (String roleName : roleNames) {
                relation = new UserAndRoleRelation();
                relation.setWxUserId(wxUserId);
                relation.setRoleName(roleName);
                relations.add(relation);
            }
            userAndRoleRelationDao.saveAll(relations);
        }
        return true;
    }

}
