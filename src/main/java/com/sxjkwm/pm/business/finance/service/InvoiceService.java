package com.sxjkwm.pm.business.finance.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.dao.UserDao;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.business.finance.dao.*;
import com.sxjkwm.pm.business.finance.dto.Order;
import com.sxjkwm.pm.business.finance.dto.WxMessageDto;
import com.sxjkwm.pm.business.finance.entity.OrderEntity;
import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.common.AbstractEventSource;
import com.sxjkwm.pm.common.PmListener;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.NuoNuoUtil;
import com.sxjkwm.pm.wxwork.listener.InvoiceMessageAnnouncer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class InvoiceService extends AbstractEventSource<WxMessageDto> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final OrderDao orderDao;

    private final InvoiceDetailDao invoiceDetailDao;

    private final VehicleInfoDao vehicleInfoDao;

    private final SecondHandCarInfoDao secondHandCarInfoDao;

    private final OperationRecordDao operationRecordDao;

    private final ProjectDao projectDao;

    private final UserDao userDao;

    @Autowired
    public InvoiceService(OrderDao orderDao, InvoiceDetailDao invoiceDetailDao,
                          VehicleInfoDao vehicleInfoDao, SecondHandCarInfoDao secondHandCarInfoDao,
                          OperationRecordDao operationRecordDao, ProjectDao projectDao, UserDao userDao) {
        this.orderDao = orderDao;
        this.invoiceDetailDao = invoiceDetailDao;
        this.vehicleInfoDao = vehicleInfoDao;
        this.secondHandCarInfoDao = secondHandCarInfoDao;
        this.operationRecordDao = operationRecordDao;
        this.projectDao = projectDao;
        this.userDao = userDao;
    }

    /**
     * 开票或冲红
     * @param projectId
     * @param taskNum
     * @param order
     * @return
     * @throws PmException
     */
    @Transactional
    public String processInvoice(Long projectId, String taskNum, Order order) throws PmException {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(order, orderEntity);
        orderEntity.setProjectId(projectId);
        orderEntity.setTaskNum(taskNum);
        orderDao.save(orderEntity);
        String invoiceSerialNum = NuoNuoUtil.processInvoice(order);
        announceUser(projectId);
        return invoiceSerialNum;
    }

    @Async
    public void announceUser(Long projectId) {
        try {
            Project project = projectDao.getOne(projectId);
            WxMessageDto messageDto = new WxMessageDto();
            messageDto.setUserId(project.getOwnerUserId());
            User user = userDao.findUserByWxUserId(project.getOwnerUserId());
            messageDto.setUsername(user.getName());
            messageDto.setProjectName(project.getProjectName());
            messageDto.setProjectCode(project.getProjectCode());
            super.pushEvent(messageDto);
        } catch (Exception e) {
            logger.error("Announce project owner failed: {}", e);
        }
    }

    @Override
    protected List<PmListener<WxMessageDto>> listeners() {
        return Lists.newArrayList(InvoiceMessageAnnouncer.getInstance());
    }

}
