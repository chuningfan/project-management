package com.sxjkwm.pm.business.flow.handler;

import com.sxjkwm.pm.business.finance.dao.OrderItemDao;
import com.sxjkwm.pm.business.finance.entity.OrderItem;
import com.sxjkwm.pm.common.PropertyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/16 20:37
 */
@Component
public class OrderItemHandler implements PropertyHandler<OrderItem> {

    private final OrderItemDao orderItemDao;

    @Autowired
    public OrderItemHandler(OrderItemDao orderItemDao) {
        this.orderItemDao = orderItemDao;
    }

    @Override
    public List<OrderItem> save(List<OrderItem> dataList) {
        return orderItemDao.saveAll(dataList);
    }

    @Override
    public List<OrderItem> update(List<OrderItem> dataList) {
        return orderItemDao.saveAll(dataList);
    }

    @Override
    public List<OrderItem> query(Long projectId, Long projectNodeId, String propertyKey) {
        OrderItem condition = new OrderItem();
        condition.setProjectId(projectId);
        condition.setProjectNodeId(projectNodeId);
        condition.setProjectNodePropertyKey(propertyKey);
        condition.setIsDeleted(0);
        Example<OrderItem> example = Example.of(condition);
        return orderItemDao.findAll(example);
    }

    @Override
    public boolean delete(List<OrderItem> dataList) {
        orderItemDao.deleteAll(dataList);
        return true;
    }
}
