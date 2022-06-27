package com.sxjkwm.pm.auditing.dao;

import com.sxjkwm.pm.auditing.entity.AuditingFlowNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:39
 */
public interface AuditingFlowNodeDao extends JpaRepository<AuditingFlowNode, Long> {

    @Query("SELECT afn FROM AuditingFlowNode afn WHERE isDeleted = 0 AND afn.auditingFlowId = ?1 AND afn.auditingStep = ?2")
    public AuditingFlowNode fetchByFlowIdAndStep(Long auditingFlowId, Integer auditingStep);

}
