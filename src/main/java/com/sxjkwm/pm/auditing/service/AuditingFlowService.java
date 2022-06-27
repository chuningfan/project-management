package com.sxjkwm.pm.auditing.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.auditing.dao.AuditingFlowDao;
import com.sxjkwm.pm.auditing.dto.AuditingFlowDto;
import com.sxjkwm.pm.auditing.entity.AuditingFlow;
import com.sxjkwm.pm.constants.Constant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:46
 */
@Service
public class AuditingFlowService {

    private final AuditingFlowDao auditingFlowDao;

    @Autowired
    public AuditingFlowService(AuditingFlowDao auditingFlowDao) {
        this.auditingFlowDao = auditingFlowDao;
    }

    public AuditingFlowDto createOrUpdate(AuditingFlowDto auditingFlowDto) {
        AuditingFlow auditingFlow = new AuditingFlow();
        auditingFlow.setId(auditingFlowDto.getId());
        auditingFlow.setName(auditingFlowDto.getName());
        auditingFlow.setIsDeleted(auditingFlowDto.getIsDeleted());
        auditingFlowDao.save(auditingFlow);
        auditingFlowDto.setId(auditingFlow.getId());
        return auditingFlowDto;
    }

    public List<AuditingFlowDto> findAvailableFlows() {
        AuditingFlow condition = new AuditingFlow();
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        List<AuditingFlow> flows = auditingFlowDao.findAll(Example.of(condition), Sort.by(Sort.Direction.ASC, ""));
        if (CollectionUtils.isEmpty(flows)) {
            return Collections.emptyList();
        }
        List<AuditingFlowDto> dataList = Lists.newArrayList();
        AuditingFlowDto dto;
        for (AuditingFlow flow: flows) {
            dto = new AuditingFlowDto();
            dto.setId(flow.getId());
            dto.setIsDeleted(flow.getIsDeleted());
            dto.setName(flow.getName());
            dataList.add(dto);
        }
        return dataList;
    }

}
