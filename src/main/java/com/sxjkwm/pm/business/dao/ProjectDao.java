package com.sxjkwm.pm.business.dao;

import com.sxjkwm.pm.business.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDao extends JpaRepository<Project, Long> {
}
