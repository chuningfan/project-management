package com.sxjkwm.pm.business.flow.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeSelectionDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlowNodeDefinitionService {

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    private final FlowNodeCollectionDefinitionService flowNodeCollectionDefinitionService;

    private final FlowNodeSelectionDefinitionService flowNodeSelectionDefinitionService;


    @Autowired
    public FlowNodeDefinitionService(FlowNodeDefinitionDao flowNodeDefinitionDao, FlowNodeCollectionDefinitionService flowNodeCollectionDefinitionService, FlowNodeSelectionDefinitionService flowNodeSelectionDefinitionService) {
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.flowNodeCollectionDefinitionService = flowNodeCollectionDefinitionService;
        this.flowNodeSelectionDefinitionService = flowNodeSelectionDefinitionService;
    }

    @Transactional
    public FlowNodeDefinitionDto saveOrUpdate(Long flowNodeId, FlowNodeDefinitionDto dto) throws SQLException, PmException {
        String propKey = dto.getPropertyKey();
        if (StringUtils.isBlank(propKey)) {
            propKey = dto.getPropertyType() + "_" + flowNodeId + "_" + (UUID.randomUUID().toString().toLowerCase().replace("-", ""));
            dto.setPropertyKey(propKey);
        }
        String propType = dto.getPropertyType();
        FlowNodeDefinition flowNodeDefinition = new FlowNodeDefinition(flowNodeId, dto);
        flowNodeDefinition = flowNodeDefinitionDao.save(flowNodeDefinition);
        Long flowNodeDefinitionId = flowNodeDefinition.getId();
        dto.setId(flowNodeDefinitionId);
        if (propType.equals("COLLECTION")) {
            List<FlowNodeCollectionDefDto> flowNodeCollectionDefDtos = dto.getFlowNodeCollectionDefDtoList();
            flowNodeCollectionDefinitionService.saveOrUpdate(dto.getFlowNodeId(), flowNodeDefinition.getId(), flowNodeCollectionDefDtos);
        }
        if (propType.equals("CHECKBOX") || propType.equals("RADIO")) {
            List<FlowNodeSelectionDefinitionDto> selectionDefinitionDtos = dto.getSelectionDtos();
            if (CollectionUtils.isNotEmpty(selectionDefinitionDtos)) {
                flowNodeSelectionDefinitionService.saveOrUpdate(flowNodeId, flowNodeDefinition.getId(), selectionDefinitionDtos);
            }
        }
        return dto;
    }

    public List<FlowNodeDefinition> getFlowNodeDefinitionList(Long flowId) {
        List<FlowNodeDefinition> nodeDefinitions = flowNodeDefinitionDao.getByFlowNodeId(flowId);
        return nodeDefinitions;
    }

    public List<FlowNodeDefinitionDto> getFlowNodeDefinitionDtoList(Long flowId) {
        List<FlowNodeDefinition> nodeDefinitions = this.getFlowNodeDefinitionList(flowId);
        if (CollectionUtils.isNotEmpty(nodeDefinitions)) {
            String collectionType = Constant.PropertyType.COLLECTION.name();
            Map<Long, List<FlowNodeCollectionDefDto>> collectionMap = null;
            if (nodeDefinitions.stream().anyMatch(d -> d.getPropertyType().equals(collectionType))) {
                List<Long> collectionTypeIds = nodeDefinitions.stream().filter(d -> d.getPropertyType().equals(collectionType)).map(FlowNodeDefinition::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(collectionTypeIds)) {
                    List<FlowNodeCollectionDefDto> collectionDefDtos = flowNodeCollectionDefinitionService.getFlowNodeCollectionDefDotsByPropDefIds(collectionTypeIds);
                    if (CollectionUtils.isNotEmpty(collectionDefDtos)) {
                        collectionMap = collectionDefDtos.stream().collect(Collectors.groupingBy(FlowNodeCollectionDefDto::getCollectionPropertyDefId));
                    }
                }
            }
            String checkboxType = Constant.PropertyType.CHECKBOX.name();
            String radioType = Constant.PropertyType.RADIO.name();
            Map<Long, List<FlowNodeSelectionDefinitionDto>> selectionDefMap = null;
            if (nodeDefinitions.stream().anyMatch(d -> d.getPropertyType().equals(checkboxType) || d.getPropertyType().equals(radioType))) {
                List<Long> selectionTypeIds = nodeDefinitions.stream().filter(d -> d.getPropertyType().equals(checkboxType) || d.getPropertyType().equals(radioType)).map(FlowNodeDefinition::getId).collect(Collectors.toList());
                selectionDefMap = flowNodeSelectionDefinitionService.getDefinitionsByDefIds(selectionTypeIds);
            }
            List<FlowNodeDefinitionDto> flowNodeDefinitionDtos = Lists.newArrayList();
            for (FlowNodeDefinition definition: nodeDefinitions) {
                FlowNodeDefinitionDto dto = new FlowNodeDefinitionDto(definition);
                if (dto.getPropertyType().equals(collectionType) && Objects.nonNull(collectionMap)) {
                    dto.setFlowNodeCollectionDefDtoList(collectionMap.get(dto.getId()));
                }
                if (Objects.nonNull(selectionDefMap) && (dto.getPropertyType().equals(checkboxType) || dto.getPropertyType().equals(radioType))) {
                    dto.setSelectionDtos(selectionDefMap.get(dto.getId()));
                }
                flowNodeDefinitionDtos.add(dto);
            }
            return flowNodeDefinitionDtos;
        }
        return Collections.emptyList();
    }

    @Transactional
    public int remove(Long id) {
        int count = flowNodeDefinitionDao.updateFlowNodeDefinition(Constant.YesOrNo.YES.getValue(), id);
        return count;
    }

    @Transactional
    public Boolean sort(List<Long> ids) {
        List<FlowNodeDefinition> definitions = flowNodeDefinitionDao.findByIds(ids);
        if (CollectionUtils.isNotEmpty(definitions)) {
            Map<Long, FlowNodeDefinition> flowNodeDefinitionMap = definitions.stream().collect(Collectors.toMap(FlowNodeDefinition::getId, fnd -> fnd));
            List<FlowNodeDefinition> saveList = Lists.newArrayList();
            for (int i = 0; i < ids.size(); i ++) {
                Long id = ids.get(i);
                FlowNodeDefinition flowNodeDefinition = flowNodeDefinitionMap.get(id);
                if (Objects.nonNull(flowNodeDefinition)) {
                    flowNodeDefinition.setPropertyIndex(i);
                    saveList.add(flowNodeDefinition);
                }
            }
            flowNodeDefinitionDao.saveAll(saveList);
            return true;
        }
        return false;
    }

}
