package com.sxjkwm.pm.business.flow.service;

import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
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
public class FlowNodeService {

    private final FlowNodeDao flowNodeDao;

    @Autowired
    public FlowNodeService(FlowNodeDao flowNodeDao) {
        this.flowNodeDao = flowNodeDao;
    }

    public List<FlowNodeDto> create(Long flowId, List<FlowNodeDto> projectFlowNodeDtos) {
        List<String> auditors;
        FlowNode projectFlowNode;
        Long flowNodeId;
        for (FlowNodeDto dto: projectFlowNodeDtos) {
//            Integer audittable = dto.getAudittable();
            projectFlowNode = new FlowNode(flowId, dto);
            projectFlowNode = flowNodeDao.save(projectFlowNode);
            flowNodeId = projectFlowNode.getId();
            dto.setId(flowNodeId);
        }
        return projectFlowNodeDtos;
    }

    public List<FlowNodeDto> update(Long flowId, List<FlowNodeDto> projectFlowNodeDtos) {
        List<FlowNodeDto> result = Lists.newArrayList();
        List<FlowNodeDto> updatedOrRemovedList = projectFlowNodeDtos.stream().filter(pfn -> Objects.nonNull(pfn.getId())).collect(Collectors.toList());
        List<FlowNodeDto> newList = projectFlowNodeDtos.stream().filter(pfn -> Objects.isNull(pfn.getId())).collect(Collectors.toList());
        FlowNode condition = new FlowNode();
        condition.setFlowId(flowId);
        List<FlowNode> existingList = flowNodeDao.findAll(Example.of(condition));
        if (CollectionUtils.isNotEmpty(existingList)) {
            List<Long> updatedIds =  updatedOrRemovedList.stream().map(FlowNodeDto::getId).collect(Collectors.toList());
            Map<Long, FlowNodeDto> updateDataMap = updatedOrRemovedList.stream().collect(Collectors.toMap(FlowNodeDto::getId, pfnd -> pfnd));
            existingList.forEach(e -> {
                if (!updatedIds.contains(e.getId())) {
                    e.setIsDeleted(Constant.YesOrNo.YES.getValue());
                } else {
                    FlowNodeDto dto = updateDataMap.get(e.getId());
                    e.setAudittable(dto.getAudittable());
                    e.setSkippable(dto.getSkippable());
                    e.setNodeName(dto.getNodeName());
                    e.setDescription(dto.getDescription());
                    e.setNodeVersion(dto.getNodeVersion());
                    e.setPatternFiles(dto.getPatternFiles());
                }
            });
            existingList = flowNodeDao.saveAll(existingList);
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            result.addAll(create(flowId, newList));
        }
        return Collections.emptyList();
    }

    public FlowNode get(Long id) {
        return flowNodeDao.getOne(id);
    }

    public List<FlowNode> getNodes(Long flowId) {
        FlowNode condition = new FlowNode();
        condition.setFlowId(flowId);
        return flowNodeDao.findAll(Example.of(condition));
    }

}
