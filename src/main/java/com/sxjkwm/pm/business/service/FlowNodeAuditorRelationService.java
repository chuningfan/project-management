package com.sxjkwm.pm.business.service;

import com.sxjkwm.pm.business.dao.FlowNodeAuditorRelationDao;
import com.sxjkwm.pm.business.entity.FlowNodeAuditorRelation;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowNodeAuditorRelationService {

    private final FlowNodeAuditorRelationDao flowNodeAuditorRelationDao;

    public FlowNodeAuditorRelationService(FlowNodeAuditorRelationDao flowNodeAuditorRelationDao) {
        this.flowNodeAuditorRelationDao = flowNodeAuditorRelationDao;
    }

    public List<FlowNodeAuditorRelation> findByFlowNodeId(Long nodeId) {
        FlowNodeAuditorRelation condition = new FlowNodeAuditorRelation();
        condition.setFlowNodeId(nodeId);
        return flowNodeAuditorRelationDao.findAll(Example.of(condition));
    }

    public List<FlowNodeAuditorRelation> create(Long flowId, Long nodeId, List<String> userIds) {
        List<FlowNodeAuditorRelation> dataList = Lists.newArrayList();
        FlowNodeAuditorRelation flowNodeAuditorRelation;
        for (String userId: userIds) {
            flowNodeAuditorRelation = new FlowNodeAuditorRelation();
            flowNodeAuditorRelation.setFlowId(flowId);
            flowNodeAuditorRelation.setFlowNodeId(nodeId);
            flowNodeAuditorRelation.setUserId(userId);
            dataList.add(flowNodeAuditorRelation);
        }
        return flowNodeAuditorRelationDao.saveAll(dataList);
    }

//    public List<FlowNodeAuditorRelation> update(List<FlowNodeAuditorRelation> flowNodeAuditorRelations) {
//
//    }

}
