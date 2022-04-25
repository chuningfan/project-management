package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentDao extends JpaRepository<Department, Long> {

    @Query(value = "SELECT d.* FROM pm_department d WHERE d.wx_dept_id IN (?1) AND d.is_deleted = 0 ", nativeQuery = true)
    List<Department> findDepartmentsByIds(List<Long> ids);

}
