package com.sxjkwm.pm.business.flow.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeSelectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeSelectionDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.flow.entity.FlowNodeSelectionDefinition;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/5/30 14:50
 */
@Service
public class FlowNodeSelectionDefinitionService {

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    private final FlowNodeSelectionDefinitionDao flowNodeSelectionDefinitionDao;

    @Autowired
    public FlowNodeSelectionDefinitionService(FlowNodeDefinitionDao flowNodeDefinitionDao, FlowNodeSelectionDefinitionDao flowNodeSelectionDefinitionDao) {
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.flowNodeSelectionDefinitionDao = flowNodeSelectionDefinitionDao;
    }

    @Transactional
    public List<FlowNodeSelectionDefinition> saveOrUpdate(Long flowNodeId, Long defId, List<FlowNodeSelectionDefinitionDto> flowNodeSelectionDtoList) throws PmException {
        FlowNodeDefinition flowNodeDefinition = flowNodeDefinitionDao.getOne(defId);
        if (Objects.isNull(flowNodeDefinition)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        String propertyType = flowNodeDefinition.getPropertyType();
        if (!"CHECKBOX".equals(propertyType) && !"RADIO".equals(propertyType)) {
            throw new PmException(PmError.INVALIDS_DATA);
        }
        List<FlowNodeSelectionDefinition> dataList = Lists.newArrayList();
        FlowNodeSelectionDefinition flowNodeSelectionDefinition;
        for (FlowNodeSelectionDefinitionDto dto: flowNodeSelectionDtoList) {
            flowNodeSelectionDefinition = new FlowNodeSelectionDefinition(dto);
            flowNodeSelectionDefinition.setRefPropDefId(defId);
            flowNodeSelectionDefinition.setFlowNodeId(flowNodeId);
            dataList.add(flowNodeSelectionDefinition);
        }
        return flowNodeSelectionDefinitionDao.saveAll(dataList);
    }

    public Map<Long, List<FlowNodeSelectionDefinitionDto>> getDefinitionsByDefIds(List<Long> defIds) {
        List<FlowNodeSelectionDefinition> definitions = flowNodeSelectionDefinitionDao.queryByDefIds(defIds);
        if (CollectionUtils.isNotEmpty(definitions)) {
            Map<Long, List<FlowNodeSelectionDefinition>> definitionMap = definitions.stream().collect(Collectors.groupingBy(FlowNodeSelectionDefinition::getRefPropDefId));
            Set<Map.Entry<Long, List<FlowNodeSelectionDefinition>>> entrySet = definitionMap.entrySet();
            Iterator<Map.Entry<Long, List<FlowNodeSelectionDefinition>>> iterator = entrySet.iterator();
            Map<Long, List<FlowNodeSelectionDefinitionDto>> resultMap = Maps.newHashMap();
            List<FlowNodeSelectionDefinitionDto> dataList;
            while (iterator.hasNext()) {
                dataList = Lists.newArrayList();
                Map.Entry<Long, List<FlowNodeSelectionDefinition>> entry = iterator.next();
                Long propDefId = entry.getKey();
                List<FlowNodeSelectionDefinition> definitionList = entry.getValue();
                definitionList = definitionList.stream().sorted(Comparator.comparing(FlowNodeSelectionDefinition::getSelectionIndex)).collect(Collectors.toList());
                for (FlowNodeSelectionDefinition definition: definitionList) {
                    dataList.add(new FlowNodeSelectionDefinitionDto(definition));
                }
                resultMap.put(propDefId, dataList);
            }
            return resultMap;
        }
        return Collections.emptyMap();
    }

    public List<FlowNodeSelectionDefinition> getByFlowNodeId(Long flowNodeId) {
        return flowNodeSelectionDefinitionDao.findByFlowNodeId(flowNodeId);
    }

}
