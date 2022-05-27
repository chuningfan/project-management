package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:28
 */
public interface FlowNodeDefinitionDao extends JpaRepository<FlowNodeDefinition, Long> {

    @Query("SELECT f FROM FlowNodeDefinition f WHERE f.isDeleted = 0 AND f.flowNodeId = ?1")
    List<FlowNodeDefinition> getByFlowNodeId(Long flowNodeId);

    @Modifying
    @Query(value = "update FlowNodeDefinition fnd set fnd.isDeleted = :isDelete where fnd.id = :id")
    int updateFlowNodeDefinition(Integer isDelete, Long id);

}
