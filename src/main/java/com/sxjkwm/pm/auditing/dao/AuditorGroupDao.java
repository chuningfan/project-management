package com.sxjkwm.pm.auditing.dao;

import com.sxjkwm.pm.auditing.entity.AuditorGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:42
 */
public interface AuditorGroupDao extends JpaRepository<AuditorGroup, Long> {

    @Query("SELECT ag FROM AuditorGroup ag WHERE isDeleted = 0 AND auditingFlowNodeId = ?1")
    List<AuditorGroup> fetchByAuditingFlowNodeId(Long auditingFlowNodeId);

}
