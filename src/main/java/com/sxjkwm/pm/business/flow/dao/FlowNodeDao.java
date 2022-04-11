package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.FlowNode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowNodeDao extends JpaRepository<FlowNode, Long> {
}
