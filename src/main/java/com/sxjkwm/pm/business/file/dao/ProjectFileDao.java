package com.sxjkwm.pm.business.file.dao;

import com.sxjkwm.pm.business.file.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/5/12 21:28
 */
public interface ProjectFileDao extends JpaRepository<ProjectFile, Long> {
}
