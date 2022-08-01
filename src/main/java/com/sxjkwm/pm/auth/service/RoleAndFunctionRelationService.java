package com.sxjkwm.pm.auth.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.RoleAndFunctionRelationDao;
import com.sxjkwm.pm.auth.entity.RoleAndFunctionRelation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleAndFunctionRelationService {

    private final RoleAndFunctionRelationDao roleAndFunctionRelationDao;

    @Autowired
    public RoleAndFunctionRelationService(RoleAndFunctionRelationDao roleAndFunctionRelationDao) {
        this.roleAndFunctionRelationDao = roleAndFunctionRelationDao;
    }

    @Transactional
    public Boolean populateRelations(String roleName, List<Long> functionIds) {
        RoleAndFunctionRelation condition = new RoleAndFunctionRelation();
        condition.setRoleName(roleName);
        Example<RoleAndFunctionRelation> example = Example.of(condition);
        List<RoleAndFunctionRelation> existingRelations = roleAndFunctionRelationDao.findAll(example);
        if (CollectionUtils.isNotEmpty(existingRelations)) {
            roleAndFunctionRelationDao.deleteInBatch(existingRelations);
        }
        if (CollectionUtils.isNotEmpty(functionIds)) {
            List<RoleAndFunctionRelation> relations = Lists.newArrayList();
            RoleAndFunctionRelation relation;
            for (Long functionId : functionIds) {
                relation = new RoleAndFunctionRelation();
                relation.setRoleName(roleName);
                relation.setFunctionId(functionId);
                relations.add(relation);
            }
            roleAndFunctionRelationDao.saveAll(relations);
        }
        return true;
    }

}
