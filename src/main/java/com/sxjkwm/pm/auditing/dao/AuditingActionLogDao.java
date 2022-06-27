package com.sxjkwm.pm.auditing.dao;

import com.sxjkwm.pm.auditing.entity.AuditingActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:44
 */
public interface AuditingActionLogDao extends JpaRepository<AuditingActionLog, Long> {
}
