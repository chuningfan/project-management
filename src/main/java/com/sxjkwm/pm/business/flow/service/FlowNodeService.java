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
    public List<FlowNodeDto> create(Long flowId, List<FlowNodeDto> flowNodeDtos) {
        FlowNode flowNode;
        Long flowNodeId;
        List<String> patternFilePaths;
        PatternFile patternFile;
        for (FlowNodeDto dto : flowNodeDtos) {
            flowNode = new FlowNode(flowId, dto);
            flowNode.setIsDeleted(Constant.YesOrNo.NO.getValue());
            flowNode = flowNodeDao.save(flowNode);
            flowNodeId = flowNode.getId();
            dto.setId(flowNodeId);
//            patternFilePaths = dto.getPatternPaths();
//            if (CollectionUtils.isNotEmpty(patternFilePaths)) {
//                List<PatternFile> patternFiles = Lists.newArrayList();
//                for (String patternFilePath: patternFilePaths) {
//                    patternFile = new PatternFile();
//                    patternFile.setFlowNodeId(flowNodeId);
//                    patternFile.setPath(patternFilePath);
//                    patternFile.setFileName(patternFilePath.substring(patternFilePath.lastIndexOf(File.separator) + 1));
//                    patternFiles.add(patternFile);
//                }
//                patternFileDao.saveAll(patternFiles);
//            }
        }
        return flowNodeDtos;
    }

    @Transactional
    public List<FlowNodeDto> update(Long flowId, List<FlowNodeDto> flowNodeDtos) {
        FlowNode condition = new FlowNode();
        condition.setFlowId(flowId);
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        condition.setSkippable(null);
        List<FlowNode> sourceList = getByConditions(condition);
        if (CollectionUtils.isEmpty(sourceList)) {
            return create(flowId, flowNodeDtos);
        } else {
            List<FlowNodeDto> res = Lists.newArrayList();
            // find new added
            List<FlowNodeDto> newNodeDtos = flowNodeDtos.stream().filter(fnd -> Objects.isNull(fnd.getId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(newNodeDtos)) {
                res.addAll(create(flowId, newNodeDtos));
            }
            List<FlowNode> dataToSave = Lists.newArrayList();
            // find removed
            // IDs not in flowNodeDtos
            List<Long> inputIds = flowNodeDtos.stream().filter(fnd -> Objects.nonNull(fnd.getId())).map(FlowNodeDto::getId).collect(Collectors.toList());
            sourceList.stream().filter(sfnd -> !inputIds.contains(sfnd.getId())).forEach(sfnd -> {
                sfnd.setIsDeleted(Constant.YesOrNo.NO.getValue());
                dataToSave.add(sfnd);
            });
            // find updated
            // IDs in flowNodeDtos
            Map<Long, FlowNodeDto> dtoMap = flowNodeDtos.stream().filter(fnd -> Objects.nonNull(fnd.getId())).collect(Collectors.toMap(FlowNodeDto::getId, fnd -> fnd, (k1, k2) -> k1));
            sourceList.stream().filter(sfnd -> inputIds.contains(sfnd.getId())).forEach(sfnd -> {
                Long id = sfnd.getId();
                FlowNodeDto flowNodeDto = dtoMap.get(id);
                sfnd.setDescription(flowNodeDto.getDescription());
                sfnd.setFormId(flowNodeDto.getFormId());
                sfnd.setNodeName(flowNodeDto.getNodeName());
                sfnd.setSkippable(flowNodeDto.getSkippable());
                dataToSave.add(sfnd);
                res.add(flowNodeDto);
            });
            if (CollectionUtils.isNotEmpty(dataToSave)) {
                flowNodeDao.saveAll(dataToSave);
            }
            return res;
        }
    }

    public FlowNode get(Long id) {
        FlowNode flowNode = new FlowNode();
        flowNode.setId(id);
        Example<FlowNode> example = Example.of(flowNode);
        return flowNodeDao.findOne(example).get();
    }

    public List<FlowNode> getByConditions(FlowNode condition, Sort sort) {
        Example<FlowNode> example = Example.of(condition);
        return flowNodeDao.findAll(example, sort);
    }

    public List<FlowNode> getByConditions(FlowNode condition) {
        Example<FlowNode> example = Example.of(condition);
        return flowNodeDao.findAll(example);
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
    public List<FlowNode> sort(List<NodeIndexDto> nodeIndexDtos) {
        List<FlowNode> list = Lists.newArrayList();
        nodeIndexDtos.forEach(nodeIndexDto -> {
            Optional<FlowNode> flowNodeOptional = flowNodeDao.findById(nodeIndexDto.getId());
            FlowNode flowNode = flowNodeOptional.get();
            flowNode.setId(nodeIndexDto.getId());
            flowNode.setNodeIndex(nodeIndexDto.getNodeIndex());
            list.add(flowNodeDao.save(flowNode));
        });
        return list.stream().sorted(Comparator.comparing(FlowNode::getNodeIndex)).collect(Collectors.toList());
    }

}
