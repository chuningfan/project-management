package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.FlowNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlowNodeDao extends JpaRepository<FlowNode, Long> {

    @Query("SELECT fn FROM FlowNode fn WHERE fn.flowId = ?1 AND fn.isDeleted = 0")
    List<FlowNode> getAvailableNodes(Long flowId);

}
