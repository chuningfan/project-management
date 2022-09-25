package com.sxjkwm.pm.business.flow.service;

import com.sxjkwm.pm.business.file.dao.PatternFileDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.dto.NodeAndDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FlowNodeService {

    private final FlowNodeDao flowNodeDao;

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    private final PatternFileDao patternFileDao;

    @Autowired
    public FlowNodeService(FlowNodeDao flowNodeDao, FlowNodeDefinitionDao flowNodeDefinitionDao, PatternFileDao patternFileDao) {
        this.flowNodeDao = flowNodeDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.patternFileDao = patternFileDao;
    }

    @Transactional
    public FlowNodeDto create(Long flowId, FlowNodeDto flowNodeDto) {
        FlowNode flowNode = new FlowNode(flowId, flowNodeDto);
        flowNode.setIsDeleted(Constant.YesOrNo.NO.getValue());
        flowNode = flowNodeDao.save(flowNode);
        Long flowNodeId = flowNode.getId();
        flowNodeDto.setId(flowNodeId);
        return flowNodeDto;
    }

    @Transactional
    public FlowNodeDto update(Long flowId, FlowNodeDto flowNodeDto) {
        FlowNode flowNode = new FlowNode(flowId, flowNodeDto);
        flowNodeDao.save(flowNode);
        return flowNodeDto;
    }

    public FlowNode get(Long id) {
        FlowNode flowNode = new FlowNode();
        flowNode.setId(id);
        Example<FlowNode> example = Example.of(flowNode);
        return flowNodeDao.findOne(example).get();
    }

    public List<FlowNode> getByFlowId(Long flowId) {
        FlowNode condition = new FlowNode();
        condition.setFlowId(flowId);
        Example<FlowNode> example = Example.of(condition);
        return flowNodeDao.findAll(example, Sort.by(Sort.Direction.fromString("ASC"), "nodeIndex"));
    }

    public List<FlowNode> getFlowNodeList(Long flowId) {
        List<FlowNode> flowNodeList = flowNodeDao.getAvailableNodes(flowId);
        return flowNodeList;
    }

    @Transactional
    public int remove(Long id) {
        int count = flowNodeDao.updateFlowNode(Constant.YesOrNo.YES.getValue(), id);
        return count;
    }


    @Transactional
    public Boolean sort(List<Long> nodeIds) {
        List<FlowNode> flowNodes = flowNodeDao.getByIds(nodeIds);
        if (CollectionUtils.isNotEmpty(flowNodes)) {
            List<FlowNode> saveList = Lists.newArrayList();
            Map<Long, FlowNode> flowNodeMap = flowNodes.stream().collect(Collectors.toMap(FlowNode::getId, fn -> fn));
            for (int i = 0; i < nodeIds.size(); i ++) {
                Long id = nodeIds.get(i);
                FlowNode flowNode = flowNodeMap.get(id);
                if (Objects.nonNull(flowNode)) {
                    flowNode.setNodeIndex(i);
                    saveList.add(flowNode);
                }
            }
            if (CollectionUtils.isNotEmpty(saveList)) {
                flowNodeDao.saveAll(saveList);
            }
            return true;
        }
        return false;
    }

    public List<NodeAndDefinitionDto> fetchNodeAndDefsByFlowId(Long flowId) throws PmException {
        FlowNode condition = new FlowNode();
        condition.setAsAvailable();
        condition.setFlowId(flowId);
        condition.setSkippable(null);
        List<FlowNode> nodes = flowNodeDao.findAll(Example.of(condition));
        if (CollectionUtils.isEmpty(nodes)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        List<Long> nodeIds = nodes.stream().map(FlowNode::getId).collect(Collectors.toList());
        List<FlowNodeDefinition> definitions = flowNodeDefinitionDao.findByFlowNodeIds(nodeIds);
        Map<Long, List<FlowNodeDefinition>> definitionsByFlowNodeId = definitions.stream().collect(Collectors.groupingBy(FlowNodeDefinition::getFlowNodeId));
        List<NodeAndDefinitionDto> resList = Lists.newArrayList();
        NodeAndDefinitionDto dto;
        List<FlowNodeDefinitionDto> definitionDtoList;
        for (FlowNode flowNode: nodes) {
            dto = new NodeAndDefinitionDto();
            dto.setFlowNodeId(flowNode.getId());
            dto.setFlowNodeName(flowNode.getNodeName());
            List<FlowNodeDefinition> relatedDefs = definitionsByFlowNodeId.get(flowNode.getId());
            if (CollectionUtils.isNotEmpty(relatedDefs)) {
                List<FlowNodeDefinitionDto> dtoList = Lists.newArrayList();
                for (FlowNodeDefinition def: relatedDefs) {
                    FlowNodeDefinitionDto definitionDto = new FlowNodeDefinitionDto();
                    definitionDto.setId(def.getId());
                    definitionDto.setPropertyName(def.getPropertyName());
                    dtoList.add(definitionDto);
                }
                dto.setDefinitionDtoList(dtoList);
                resList.add(dto);
            }
        }
        return resList;
    }

}
