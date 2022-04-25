package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.UserAndRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAndRoleRelationDao extends JpaRepository<UserAndRoleRelation, Long> {

    @Query("SELECT t.roleId FROM UserAndRoleRelation t WHERE t.wxUserId=?1")
    List<Long> findRoleIdsByWxUserId(String wxUserId);

}
