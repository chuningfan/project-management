package com.sxjkwm.pm.business.flow.service;

import com.sxjkwm.pm.business.file.dao.PatternFileDao;
import com.sxjkwm.pm.business.file.entity.PatternFile;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDto;
import com.sxjkwm.pm.business.flow.dto.NodeIndexDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.constants.Constant;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FlowNodeService {

    private final FlowNodeDao flowNodeDao;

    private final PatternFileDao patternFileDao;

    @Autowired
    public FlowNodeService(FlowNodeDao flowNodeDao, PatternFileDao patternFileDao) {
        this.flowNodeDao = flowNodeDao;
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
        Integer isDelete = Constant.YesOrNo.NO.getValue();
        List<FlowNode> flowNodeList = flowNodeDao.getAllByFlowIdAndIsDeleted(flowId, isDelete);
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

}
