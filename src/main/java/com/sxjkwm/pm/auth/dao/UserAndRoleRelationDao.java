package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.UserAndRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserAndRoleRelationDao extends JpaRepository<UserAndRoleRelation, Long> {

    @Query("SELECT t.roleName FROM UserAndRoleRelation t WHERE t.wxUserId=?1 AND t.isDeleted = 0")
    List<String> findRoleNamesByWxUserId(String wxUserId);

    @Query("SELECT t FROM UserAndRoleRelation t WHERE t.wxUserId IN (?1) AND t.isDeleted = 0")
    List<UserAndRoleRelation> findByWxUserIds(List<String> wxUserIds);

}
