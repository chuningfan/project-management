package com.sxjkwm.pm.business.flow.service;

import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.constants.Constant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FlowNodeDefinitionService {

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    @Autowired
    public FlowNodeDefinitionService(FlowNodeDefinitionDao flowNodeDefinitionDao) {
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
    }

    @Transactional
    public List<FlowNodeDefinitionDto> create(Long flowNodeId, List<FlowNodeDefinitionDto> flowNodeDefinitionDtos) {
        FlowNodeDefinition flowNodeDefinition;
        Long flowNodeDefinitionId;
        for (FlowNodeDefinitionDto dto: flowNodeDefinitionDtos) {
            flowNodeDefinition = new FlowNodeDefinition(flowNodeId, dto);
            flowNodeDefinition = flowNodeDefinitionDao.save(flowNodeDefinition);
            flowNodeDefinitionId = flowNodeDefinition.getId();
            dto.setId(flowNodeDefinitionId);
        }
        return flowNodeDefinitionDtos;
    }

    public List<FlowNodeDefinition> getFlowNodeDefinitionList(Long flowId) {
        List<FlowNodeDefinition> nodeDefinitions = flowNodeDefinitionDao.getByFlowNodeId(flowId);
        return nodeDefinitions;
    }

    @Transactional
    public int remove(Long id) {
        int count = flowNodeDefinitionDao.updateFlowNodeDefinition(Constant.YesOrNo.YES.getValue(), id);
        return count;
    }

    @Transactional
    public List<FlowNodeDefinitionDto> update(Long flowNodeId, List<FlowNodeDefinitionDto> flowNodeDefinitionDtos) {
        // 1、查询当前flowNodeId对应的数据
        FlowNodeDefinition condition = new FlowNodeDefinition();
        condition.setFlowNodeId(flowNodeId);
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        Example<FlowNodeDefinition> example = Example.of(condition);
        List<FlowNodeDefinition> sourceList = flowNodeDefinitionDao.findAll(example);
        // 2、根据flowNodeId查询数据为空，新增数据
        if (CollectionUtils.isEmpty(sourceList)) {
            return create(flowNodeId, flowNodeDefinitionDtos);
        } else {
            // id为空，新增数据
            List<FlowNodeDefinitionDto> res = Lists.newArrayList();
            List<FlowNodeDefinitionDto> newNodeDefinitionDtos = flowNodeDefinitionDtos.stream().filter(fnd -> Objects.isNull(fnd.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(newNodeDefinitionDtos)) {
                res.addAll(create(flowNodeId, flowNodeDefinitionDtos));
            }
            List<FlowNodeDefinition> dataToSave = Lists.newArrayList();
            List<Long> inputIds = flowNodeDefinitionDtos.stream().filter(fnd -> Objects.nonNull(fnd.getId())).map(FlowNodeDefinitionDto::getId).collect(Collectors.toList());
            sourceList.stream().filter(sfnd -> !inputIds.contains(sfnd.getId())).forEach(sfnd -> {
                sfnd.setIsDeleted(Constant.YesOrNo.NO.getValue());
                dataToSave.add(sfnd);
            });
            Map<Long, FlowNodeDefinitionDto> dtoMap = flowNodeDefinitionDtos.stream().filter(fnd -> Objects.nonNull(fnd.getId())).collect(Collectors.toMap(FlowNodeDefinitionDto::getId, fnd -> fnd, (k1, k2) -> k1));
            sourceList.stream().filter(sfnd -> inputIds.contains(sfnd.getId())).forEach(sfnd -> {
                Long id = sfnd.getId();
                FlowNodeDefinitionDto flowNodeDefinitionDto = dtoMap.get(id);
                if (StringUtils.isNotEmpty(flowNodeDefinitionDto.getCollectionPropertyHandler())) {
                    sfnd.setCollectionPropertyHandler(flowNodeDefinitionDto.getCollectionPropertyHandler());
                }
                if (!Objects.isNull(flowNodeDefinitionDto.getPropertyIndex())) {
                    sfnd.setPropertyIndex(flowNodeDefinitionDto.getPropertyIndex());
                }
                if (StringUtils.isNotEmpty(flowNodeDefinitionDto.getPropertyKey())) {
                    sfnd.setPropertyKey(flowNodeDefinitionDto.getPropertyKey());
                }
                if (StringUtils.isNotEmpty(flowNodeDefinitionDto.getPropertyName())) {
                    sfnd.setPropertyName(flowNodeDefinitionDto.getPropertyName());
                }
                if (StringUtils.isNotEmpty(flowNodeDefinitionDto.getPropertyType())) {
                    sfnd.setPropertyType(flowNodeDefinitionDto.getPropertyType());
                }
                dataToSave.add(sfnd);
                res.add(flowNodeDefinitionDto);
            });
            if (CollectionUtils.isNotEmpty(dataToSave)) {
                flowNodeDefinitionDao.saveAll(dataToSave);
            }
            return res;
        }
    }
}
