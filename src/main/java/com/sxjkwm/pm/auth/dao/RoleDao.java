package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r.* FROM pm_role r WHERE r.id IN (:ids) AND r.is_deleted = 0 ", nativeQuery = true)
    List<Role> findRolesByIds(@Param("ids") List<Long> ids);

    @Query("SELECT r FROM Role r WHERE r.name = ?1 AND r.isDeleted = 0 ")
    Role findByRoleName(String roleName);

    @Query(value = "SELECT r.* FROM pm_role r WHERE r.id IN (:names) AND r.is_deleted = 0 ", nativeQuery = true)
    List<Role> findRolesByNames(@Param("names") List<String> names);

}
