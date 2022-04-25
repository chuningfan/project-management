package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Long> {

    @Query(value = "SELECT r.* FROM pm_role r WHERE r.id IN (:ids) AND r.is_deleted = 0 ", nativeQuery = true)
    List<Role> findRolesByIds(@Param("ids") List<Long> ids);

}
