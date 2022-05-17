package com.sxjkwm.pm.business.finance.dao;

import com.sxjkwm.pm.business.finance.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/5/16 20:38
 */
public interface OrderItemDao extends JpaRepository<OrderItem, Long> {
}
