package com.sxjkwm.pm.business.finance.dao;

import com.sxjkwm.pm.business.finance.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<OrderEntity, Long> {
}
