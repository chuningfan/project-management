package com.sxjkwm.pm.auditing.service;

import com.sxjkwm.pm.auditing.dao.AuditorGroupDao;
import com.sxjkwm.pm.auditing.entity.AuditorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/9 15:31
 */
@Service
public class AuditorGroupService {

    private final AuditorGroupDao auditorGroupDao;

    @Autowired
    public AuditorGroupService(AuditorGroupDao auditorGroupDao) {
        this.auditorGroupDao = auditorGroupDao;
    }

    public List<AuditorGroup> fetchByAuditingFlowNodeId(Long auditingFlowNodeId) {
        return auditorGroupDao.fetchByAuditingFlowNodeId(auditingFlowNodeId);
    }

}
