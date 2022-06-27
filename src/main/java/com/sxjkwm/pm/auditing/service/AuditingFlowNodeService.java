package com.sxjkwm.pm.auditing.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auditing.constant.AuditingConstant;
import com.sxjkwm.pm.auditing.dao.AuditingFlowNodeDao;
import com.sxjkwm.pm.auditing.dao.AuditorGroupDao;
import com.sxjkwm.pm.auditing.dto.AuditingFlowNodeDto;
import com.sxjkwm.pm.auditing.dto.AuditorGroupDto;
import com.sxjkwm.pm.auditing.entity.AuditingFlowNode;
import com.sxjkwm.pm.auditing.entity.AuditorGroup;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/6/7 18:56
 */
@Service
public class AuditingFlowNodeService {

    private final AuditingFlowNodeDao auditingFlowNodeDao;

    private final AuditorGroupDao auditorGroupDao;

    @Autowired
    public AuditingFlowNodeService(AuditingFlowNodeDao auditingFlowNodeDao, AuditorGroupDao auditorGroupDao) {
        this.auditingFlowNodeDao = auditingFlowNodeDao;
        this.auditorGroupDao = auditorGroupDao;
    }

    @Transactional
    public AuditingFlowNodeDto saveOrUpdate(AuditingFlowNodeDto auditingFlowNodeDto) {
        AuditingFlowNode auditingFlowNode = new AuditingFlowNode(auditingFlowNodeDto);
        auditingFlowNodeDao.save(auditingFlowNode);
        Integer auditingType = auditingFlowNodeDto.getAuditingType();
        List<AuditorGroupDto> combineApproveGroups = auditingFlowNodeDto.getCombineApproveGroups();
        if (AuditingConstant.AuditingType.OR.getType().equals(auditingType)) {
            AuditorGroupDto singleApproveGroup = combineApproveGroups.get(0);
            Long groupId = singleApproveGroup.getId();
            List<String> relatedUserIds = singleApproveGroup.getUserIds();
            if (CollectionUtils.isNotEmpty(relatedUserIds)) {
                String userIds = Joiner.on(",").join(relatedUserIds);
                AuditorGroup auditorGroup = new AuditorGroup();
                auditorGroup.setId(groupId);
                auditorGroup.setAuditingFlowNodeId(auditingFlowNodeDto.getAuditingFlowId());
                auditorGroup.setUserIds(userIds);
                auditorGroupDao.save(auditorGroup);
            }
        } else if (AuditingConstant.AuditingType.AND.getType().equals(auditingType)) {
            List<AuditorGroup> groups = Lists.newArrayList();
            AuditorGroup auditorGroup;
            for (AuditorGroupDto dto: combineApproveGroups) {
                Long groupId = dto.getId();
                List<String> relatedUserIds = dto.getUserIds();
                String userIds = Joiner.on(",").join(relatedUserIds);
                if (CollectionUtils.isNotEmpty(relatedUserIds)) {
                    auditorGroup = new AuditorGroup();
                    auditorGroup.setId(groupId);
                    auditorGroup.setAuditingFlowNodeId(auditingFlowNodeDto.getAuditingFlowId());
                    auditorGroup.setUserIds(userIds);
                    groups.add(auditorGroup);
                }
            }
            if (CollectionUtils.isNotEmpty(groups)) {
                auditorGroupDao.saveAll(groups);
            }
        } else {
            return null;
        }
        return auditingFlowNodeDto;
    }

    public AuditingFlowNodeDto getByFlowNodeId(Long flowNodeId) {
        AuditingFlowNode auditingFlowNode = auditingFlowNodeDao.getOne(flowNodeId);
        if (Objects.nonNull(auditingFlowNode)) {
            AuditingFlowNodeDto dto = new AuditingFlowNodeDto();
            dto.setAuditingFlowId(auditingFlowNode.getAuditingFlowId());
            dto.setAuditingType(auditingFlowNode.getAuditingType());
            dto.setId(auditingFlowNode.getId());
            List<AuditorGroup> auditorGroups = auditorGroupDao.fetchByAuditingFlowNodeId(flowNodeId);
            if (CollectionUtils.isNotEmpty(auditorGroups)) {
                List<AuditorGroupDto> groupDtos = Lists.newArrayList();
                AuditorGroupDto groupDto;
                for (AuditorGroup group: auditorGroups) {
                    groupDto = new AuditorGroupDto();
                    Long groupId = group.getId();
                    String userIds = group.getUserIds();
                    groupDto.setId(groupId);
                    if (StringUtils.isNotBlank(userIds)) {
                        groupDto.setUserIds(Splitter.on(",").splitToList(userIds));
                    }
                    groupDtos.add(groupDto);
                }
                dto.setCombineApproveGroups(groupDtos);
            }
            return dto;
        }
        return null;
    }

    public List<AuditingFlowNode> findAllByFlowId(Long flowId) {
        AuditingFlowNode condition = new AuditingFlowNode();
        condition.setAuditingFlowId(flowId);
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        return auditingFlowNodeDao.findAll(Example.of(condition), Sort.by(Sort.Direction.ASC, "auditingStep"));
    }

    public AuditingFlowNode findNext(Long flowId, Long currentFlowNodeId) throws PmException {
        List<AuditingFlowNode> nodes = findAllByFlowId(flowId);
        if (CollectionUtils.isEmpty(nodes)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        boolean flag = false;
        for (AuditingFlowNode node: nodes) {
            if (node.getId().equals(currentFlowNodeId)) {
                flag = true;
            }
            if (flag) {
                return node;
            }
        }
        return null;
    }

    public AuditingFlowNode fetchByFlowIdAndStep(Long auditingFlowId, Integer auditingStep) {
        return auditingFlowNodeDao.fetchByFlowIdAndStep(auditingFlowId, auditingStep);
    }

}
