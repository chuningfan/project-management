package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.FlowNodeSelectionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/30 14:33
 */
public interface FlowNodeSelectionDefinitionDao extends JpaRepository<FlowNodeSelectionDefinition, Long> {

    @Query("SELECT fsd FROM FlowNodeSelectionDefinition fsd WHERE fsd.isDeleted = 0 AND fsd.refPropDefId IN (?1)")
    List<FlowNodeSelectionDefinition> queryByDefIds(List<Long> defIds);

    @Query("SELECT fsd FROM FlowNodeSelectionDefinition fsd WHERE fsd.isDeleted = 0 AND fsd.flowNodeId = ?1")
    List<FlowNodeSelectionDefinition> findByFlowNodeId(Long flowNodeId);

}
