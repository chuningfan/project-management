package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:28
 */
public interface FlowNodeDefinitionDao extends JpaRepository<FlowNodeDefinition, Long> {
}
