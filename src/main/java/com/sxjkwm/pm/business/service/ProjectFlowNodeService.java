package com.sxjkwm.pm.business.service;

import com.sxjkwm.pm.business.dao.FlowNodeAuditorRelationDao;
import com.sxjkwm.pm.business.dao.ProjectFlowNodeDao;
import com.sxjkwm.pm.business.dto.ProjectFlowNodeDto;
import com.sxjkwm.pm.business.entity.FlowNodeAuditorRelation;
import com.sxjkwm.pm.business.entity.ProjectFlowNode;
import com.sxjkwm.pm.constants.Constant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectFlowNodeService {

    private final ProjectFlowNodeDao projectFlowNodeDao;

    private final FlowNodeAuditorRelationDao flowNodeAuditorRelationDao;

    @Autowired
    public ProjectFlowNodeService(ProjectFlowNodeDao projectFlowNodeDao, FlowNodeAuditorRelationDao flowNodeAuditorRelationDao) {
        this.projectFlowNodeDao = projectFlowNodeDao;
        this.flowNodeAuditorRelationDao = flowNodeAuditorRelationDao;
    }

    public List<ProjectFlowNodeDto> create(Long flowId, List<ProjectFlowNodeDto> projectFlowNodeDtos) {
        Integer audittable;
        List<String> auditors;
        ProjectFlowNode projectFlowNode;
        Long flowNodeId;
        for (ProjectFlowNodeDto dto: projectFlowNodeDtos) {
            audittable = dto.getAudittable();
            projectFlowNode = new ProjectFlowNode(flowId, dto);
            projectFlowNode = projectFlowNodeDao.save(projectFlowNode);
            auditors = dto.getAuditors();
            flowNodeId = projectFlowNode.getId();
            dto.setId(flowNodeId);
            if (Objects.nonNull(audittable) && Constant.YesOrNo.YES.getValue().equals(audittable) && CollectionUtils.isNotEmpty(auditors)) {
                List<FlowNodeAuditorRelation> relations = Lists.newArrayList();
                FlowNodeAuditorRelation relation;
                for (String userId: auditors) {
                    relation = new FlowNodeAuditorRelation();
                    relation.setFlowNodeId(flowNodeId);
                    relation.setFlowId(flowId);
                    relation.setUserId(userId);
                    relations.add(relation);
                }
                flowNodeAuditorRelationDao.saveAll(relations);
            }
        }
        return projectFlowNodeDtos;
    }

    public List<ProjectFlowNodeDto> update(Long flowId, List<ProjectFlowNodeDto> projectFlowNodeDtos) {
        List<ProjectFlowNodeDto> result = Lists.newArrayList();
        List<ProjectFlowNodeDto> updatedOrRemovedList = projectFlowNodeDtos.stream().filter(pfn -> Objects.nonNull(pfn.getId())).collect(Collectors.toList());
        List<ProjectFlowNodeDto> newList = projectFlowNodeDtos.stream().filter(pfn -> Objects.isNull(pfn.getId())).collect(Collectors.toList());
        ProjectFlowNode condition = new ProjectFlowNode();
        condition.setFlowId(flowId);
        List<ProjectFlowNode> existingList = projectFlowNodeDao.findAll(Example.of(condition));
        if (CollectionUtils.isNotEmpty(existingList)) {
            List<Long> updatedIds =  updatedOrRemovedList.stream().map(ProjectFlowNodeDto::getId).collect(Collectors.toList());
            Map<Long, ProjectFlowNodeDto> updateDataMap = updatedOrRemovedList.stream().collect(Collectors.toMap(ProjectFlowNodeDto::getId, pfnd -> pfnd));
            existingList.forEach(e -> {
                if (!updatedIds.contains(e.getId())) {
                    e.setIsDeleted(Constant.YesOrNo.YES.getValue());
                } else {
                    ProjectFlowNodeDto dto = updateDataMap.get(e.getId());
                    e.setAudittable(dto.getAudittable());
                    e.setSkippable(dto.getSkippable());
                    e.setNodeName(dto.getNodeName());
                    e.setDescription(dto.getDescription());
                    e.setNodeVersion(dto.getNodeVersion());
                    e.setPatternFiles(dto.getPatternFiles());
                }
            });
            existingList = projectFlowNodeDao.saveAll(existingList);
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            result.addAll(create(flowId, newList));
        }
        return Collections.emptyList();
    }

    public ProjectFlowNode get(Long id) {
        return projectFlowNodeDao.getOne(id);
    }

    public List<ProjectFlowNode> getNodes(Long flowId) {
        ProjectFlowNode condition = new ProjectFlowNode();
        condition.setFlowId(flowId);
        return projectFlowNodeDao.findAll(Example.of(condition));
    }

}
