package com.sxjkwm.pm.business.flow.service;

import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FlowNodeDefinitionService {

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

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
            dto.setId(flowNodeId);
        }
        return flowNodeDefinitionDtos;
    }

    public Page<FlowNodeDefinition> getFlowNodeDefinitionList(Integer pageNum, Integer pageSize, Long flowId){
        if (pageNum <= 1) {
            pageNum = 0;
        } else {
            pageNum -= 1;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<FlowNodeDefinition> page = flowNodeDefinitionDao.getAllByFlowNodeId(pageable,flowId);
        return page;
    }



    @Transactional
    public int remove(Long id) {
        int count = flowNodeDefinitionDao.updateFlowNodeDefinition(Constant.YesOrNo.YES.getValue(), id);
        return count;
    }
}
