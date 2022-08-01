package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.RoleAndFunctionRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleAndFunctionRelationDao extends JpaRepository<RoleAndFunctionRelation, Long> {

    @Query(value = "SELECT t.* FROM pm_role_function_relation t WHERE t.role_id IN (?1)", nativeQuery = true)
    List<RoleAndFunctionRelation> findRoleAndFunctionRelationsByRoleIds(List<Long> roleIds);

    @Query(value = "SELECT t.* FROM pm_role_function_relation t WHERE t.role_name IN (?1)", nativeQuery = true)
    List<RoleAndFunctionRelation> findRoleAndFunctionRelationsByRoleNames(List<String> roleNames);

}
