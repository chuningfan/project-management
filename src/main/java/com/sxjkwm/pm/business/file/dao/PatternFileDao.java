package com.sxjkwm.pm.business.file.dao;

import com.sxjkwm.pm.business.file.entity.PatternFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatternFileDao extends JpaRepository<PatternFile, Long> {
}
