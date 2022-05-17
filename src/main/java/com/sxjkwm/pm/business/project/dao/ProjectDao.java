package com.sxjkwm.pm.business.project.dao;

import com.sxjkwm.pm.business.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectDao extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
}
