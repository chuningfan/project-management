package com.sxjkwm.pm.business.flow.service;

import com.sxjkwm.pm.business.flow.dao.FlowDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowDto;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FlowService {

    private final FlowDao flowDao;

    @Autowired
    public FlowService(FlowDao flowDao, FlowNodeDao flowNodeDao, FlowNodeDefinitionDao flowNodeDefinitionDao) {
        this.flowDao = flowDao;
    }

    public Flow createFlow(Flow flow) {
        return flowDao.save(flow);
    }

    @Transactional
    public Flow updateFlow(FlowDto flowDto) {
        Long id = flowDto.getId();
        Flow source = getFlow(id);
        source.setFlowName(flowDto.getFlowName());
        source.setIsDeleted(flowDto.getIsDeleted());
        source.setDescription(flowDto.getDescription());
        flowDao.save(source);
        return source;
    }

    public Flow getFlow(Long id) {
        Flow flow = new Flow();
        flow.setId(id);
        Example<Flow> example = Example.of(flow);
        Flow source = flowDao.findOne(example).get();
        return source;
    }

    public List<Flow> getFlows(String flowName) {
        if (StringUtils.isBlank(flowName)) {
            return flowDao.findAll();
        }
        Flow flow = new Flow();
        flow.setFlowName(flowName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("flowName", ExampleMatcher.GenericPropertyMatchers.contains());
        return flowDao.findAll(Example.of(flow, matcher));
    }


    public Page<Flow> getFlowList(Integer pageNum, Integer pageSize, String flowName) {
        if (pageNum <= 1) {
            pageNum = 0;
        } else {
            pageNum -= 1;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Flow> page = null;
        Integer isDelete = Constant.YesOrNo.NO.getValue();
        if (StringUtils.isEmpty(flowName)) {
            page = flowDao.findByIsDeleted(isDelete, pageable);
        }
        if (StringUtils.isNotEmpty(flowName)) {
            page = flowDao.findByIsDeletedAndFlowNameLike(isDelete, "%" + flowName + "%", pageable);
        }
        return page;
    }

    @Transactional
    public int remove(Long id) {
        int count = flowDao.updateFlow(Constant.YesOrNo.YES.getValue(), id);
        return count;
    }

}
