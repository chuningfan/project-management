package com.sxjkwm.pm.auditing.dao;

import com.sxjkwm.pm.auditing.entity.AuditingFlow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:38
 */
public interface AuditingFlowDao extends JpaRepository<AuditingFlow, Long> {
}
