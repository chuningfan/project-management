package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectDao extends JpaRepository<Project, Long> {
}
