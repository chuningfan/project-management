package com.sxjkwm.pm.auditing.dao;

import com.sxjkwm.pm.auditing.entity.AuditingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:43
 */
public interface AuditingRecordDao extends JpaRepository<AuditingRecord, Long> {
}
