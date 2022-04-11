package com.sxjkwm.pm.business.flow.dao;

import com.sxjkwm.pm.business.flow.entity.Flow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowDao extends JpaRepository<Flow, Long> {
}
