package com.sxjkwm.pm.auditing.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auditing.constant.AuditingConstant;
import com.sxjkwm.pm.auditing.dao.AuditingActionLogDao;
import com.sxjkwm.pm.auditing.dao.AuditingRecordDao;
import com.sxjkwm.pm.auditing.dto.AuditingDataDto;
import com.sxjkwm.pm.auditing.dto.AuditingFlowNodeDto;
import com.sxjkwm.pm.auditing.entity.AuditingActionLog;
import com.sxjkwm.pm.auditing.entity.AuditingFlowNode;
import com.sxjkwm.pm.auditing.entity.AuditingRecord;
import com.sxjkwm.pm.auditing.entity.AuditorGroup;
import com.sxjkwm.pm.auditing.handler.AuditingDataHandler;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.WxWorkMessageUtil;
import com.sxjkwm.pm.wxwork.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/6/8 9:39
 */
@Service
public class AuditingActionService {

    private static final Logger logger = LoggerFactory.getLogger(AuditingActionService.class);

    private final AuditingRecordDao auditingRecordDao;

    private final AuditingFlowNodeService auditingFlowNodeService;

    private final AuditorGroupService auditorGroupService;

    private final AuditingActionLogDao auditingActionLogDao;

    private final RedissonClient redissonClient;

    private final UserService userService;

    private static final String lockPrefix = "auditingLock:";

    @Autowired
    public AuditingActionService(AuditingRecordDao auditingRecordDao, AuditingFlowNodeService auditingFlowNodeService, AuditorGroupService auditorGroupService, AuditingActionLogDao auditingActionLogDao, RedissonClient redissonClient, UserService userService) {
        this.auditingRecordDao = auditingRecordDao;
        this.auditingFlowNodeService = auditingFlowNodeService;
        this.auditorGroupService = auditorGroupService;
        this.auditingActionLogDao = auditingActionLogDao;
        this.redissonClient = redissonClient;
        this.userService = userService;
    }

    @Transactional
    public Boolean start(Integer auditingDataType, Long dataId, Long auditingFlowId, Long businessFlowNodeId, Boolean onlySave) throws PmException {
        if (Objects.isNull(auditingDataType)) {
            auditingDataType = 1;
        }
        UserDataDto userDataDto = ContextHelper.getUserData();
        if (Objects.isNull(userDataDto)) {
            throw new PmException(PmError.USER_NOT_LOGGED_IN);
        }
        String userId = userDataDto.getWxUserId();
        AuditingRecord auditingRecord = new AuditingRecord();
        if (Objects.nonNull(onlySave) && onlySave) {
            auditingRecord.setAuditingStatus(AuditingConstant.AuditingStatus.SAVED.getStatus());
        } else {
            auditingRecord.setAuditingStatus(AuditingConstant.AuditingStatus.IN_PROGRESS.getStatus());
        }
        auditingRecord.setAuditingFlowId(auditingFlowId);
        auditingRecord.setDataId(dataId);
        auditingRecord.setDataType(auditingDataType);
        auditingRecord.setUserId(userId);
        auditingRecord.setBusinessFlowNodeId(businessFlowNodeId);
        List<AuditingFlowNode> flowNodes = auditingFlowNodeService.findAllByFlowId(auditingFlowId);
        if (CollectionUtils.isEmpty(flowNodes)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        Integer dataTypeValue = auditingRecord.getDataType();
        AuditingConstant.AuditingDataType dataType = AuditingConstant.AuditingDataType.getFromType(dataTypeValue);
        AuditingDataHandler auditingDataHandler = dataType.dataHandler();
        List<AuditingDataDto> auditingDataDtos = auditingDataHandler.readData(dataId, businessFlowNodeId);
        if (CollectionUtils.isEmpty(auditingDataDtos)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        String dataString = JSONArray.toJSONString(auditingDataDtos);
        auditingRecord.setAuditingData(dataString);
        AuditingFlowNode firstNode = flowNodes.get(0);
        auditingRecord.setCurrentAuditingStep(firstNode.getAuditingStep());
        auditingRecordDao.save(auditingRecord);
        AuditingFlowNode firstAuditingNode = auditingFlowNodeService.fetchByFlowIdAndStep(auditingFlowId, 0);
        announceNodeAuditors(auditingRecord.getId(), dataId, firstAuditingNode, AuditingConstant.AuditingDataType.getFromType(auditingDataType));
        return true;
    }

    @Transactional
    public Boolean abort(Long auditingRecordId) {
        AuditingRecord auditingRecord = auditingRecordDao.getOne(auditingRecordId);
        if (Objects.nonNull(auditingRecord)) {
            auditingRecord.setAuditingStatus(AuditingConstant.AuditingStatus.ABORTED.getStatus());
            auditingRecordDao.save(auditingRecord);
            return true;
        }
        return false;
    }

    /**
     * 签字并通过
     * @return
     */
    @Transactional
    public Boolean sign(MultipartFile signFile, Long auditingRecordId, Long auditingFlowNodeId) throws PmException {
        RLock lock = redissonClient.getLock(lockPrefix + auditingRecordId);
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                AuditingRecord auditingRecord = auditingRecordDao.getOne(auditingRecordId);
                if (Objects.nonNull(auditingRecord)
                        && !auditingRecord.getAuditingStatus().equals(AuditingConstant.AuditingStatus.COMPLETED.getStatus())
                        && !auditingRecord.getAuditingStatus().equals(AuditingConstant.AuditingStatus.REJECTED.getStatus())
                        && !auditingRecord.getAuditingStatus().equals(AuditingConstant.AuditingStatus.ABORTED.getStatus())) {
                    AuditingFlowNodeDto auditingFlowNodeDto = auditingFlowNodeService.getByFlowNodeId(auditingFlowNodeId);
                    if (auditingRecord.getCurrentAuditingStep().intValue() >= auditingFlowNodeDto.getStep().intValue()) {
                        throw new PmException(PmError.INVALID_DATA_STATUS);
                    }
                    AuditingActionLog auditingActionLog = new AuditingActionLog();
                    auditingActionLog.setAuditingAction(AuditingConstant.Action.APPROVE.getAction());
                    auditingActionLog.setAuditingRecordId(auditingRecordId);
                    auditingActionLog.setAuditingFlowNodeId(auditingFlowNodeId);
                    auditingActionLog.seteSign(signFile.getBytes());
                    auditingActionLog.setOperationUserId(ContextHelper.getUserData().getWxUserId());
                    auditingActionLog.setIsDeleted(0);
                    auditingActionLogDao.save(auditingActionLog);
                    Long flowId = auditingRecord.getAuditingFlowId();
                    // See the auditing type of current node
                    List<AuditorGroup> groups = auditorGroupService.fetchByAuditingFlowNodeId(auditingFlowNodeId);
                    if (CollectionUtils.isEmpty(groups)) {
                        throw new PmException(PmError.NO_DATA_FOUND);
                    }
                    AuditingFlowNode nextNode = auditingFlowNodeService.findNext(flowId, auditingFlowNodeId);
                    if (Objects.isNull(nextNode)) { // 审批完成
                        auditingRecord.setAuditingStatus(AuditingConstant.AuditingStatus.COMPLETED.getStatus());
                        auditingRecordDao.save(auditingRecord);
                        announceStarter(auditingRecord.getUserId());
                        return true;
                    }
                    if (groups.size() > 1) { // 该节点为会签组
                        AuditingActionLog condition = new AuditingActionLog();
                        condition.setAuditingRecordId(auditingRecordId);
                        condition.setAuditingFlowNodeId(auditingFlowNodeId);
                        List<AuditingActionLog> logs = auditingActionLogDao.findAll(Example.of(condition));
                        if (CollectionUtils.isNotEmpty(logs)) {
                            List<Long> signedGroupIds = Lists.newArrayList();
                            List<String> signedAuditors = logs.stream().map(AuditingActionLog::getOperationUserId).collect(Collectors.toList());
                            for (AuditorGroup group : groups) {
                                if (signedGroupIds.contains(group.getId())) {
                                    continue;
                                }
                                String userIds = group.getUserIds();
                                List<String> groupAuditors = Splitter.on(",").splitToList(userIds);
                                for (String signedAuditor : signedAuditors) {
                                    if (groupAuditors.contains(signedAuditor)) {
                                        signedGroupIds.add(group.getId());
                                        break;
                                    }
                                }
                            }
                            if (signedGroupIds.size() == groups.size()) { // 会签组已经签完
                                Integer dataType = auditingRecord.getDataType();
                                Long dataId = auditingRecord.getDataId();
                                AuditingConstant.AuditingDataType auditingDataType = AuditingConstant.AuditingDataType.getFromType(dataType);
                                return announceNodeAuditors(auditingRecordId, dataId, nextNode, auditingDataType);
                            }
                        }
                        return true;
                    }
                }
            }
            throw new PmException(PmError.INVALID_DATA_STATUS);
        } catch (Exception e) {
            if (e instanceof PmException) {
                throw (PmException)e;
            } else {
                throw new PmException(e.getMessage());
            }
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    public Boolean reject(Long auditingRecordId) throws PmException {
        RLock lock = redissonClient.getLock(lockPrefix + auditingRecordId);
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                AuditingRecord auditingRecord = auditingRecordDao.getOne(auditingRecordId);
                if (Objects.nonNull(auditingRecord)) {
                    Long flowId = auditingRecord.getAuditingFlowId();
                    Integer step = auditingRecord.getCurrentAuditingStep();
                    AuditingFlowNode auditingFlowNode = auditingFlowNodeService.fetchByFlowIdAndStep(flowId, step);
                    auditingRecord.setAuditingStatus(AuditingConstant.AuditingStatus.REJECTED.getStatus());
                    auditingRecordDao.save(auditingRecord);
                    AuditingActionLog auditingActionLog = new AuditingActionLog();
                    auditingActionLog.setAuditingAction(AuditingConstant.Action.REJECT.getAction());
                    auditingActionLog.setAuditingRecordId(auditingRecordId);
                    auditingActionLog.setAuditingFlowNodeId(Objects.isNull(auditingFlowNode) ? null : auditingFlowNode.getId());
                    auditingActionLog.setOperationUserId(ContextHelper.getUserData().getWxUserId());
                    auditingActionLog.setIsDeleted(0);
                    auditingActionLogDao.save(auditingActionLog);
                    return true;
                }
            }
        } catch (Exception e) {
            throw new PmException(e.getMessage());
        } finally {
            lock.unlock();
        }
        return false;
    }


    @Async
    public boolean announceNodeAuditors(Long auditorRecordId, Long dataId, AuditingFlowNode node, AuditingConstant.AuditingDataType auditingDataType) {
        List<AuditorGroup> groups = auditorGroupService.fetchByAuditingFlowNodeId(node.getId());
        if (CollectionUtils.isEmpty(groups)) {
            logger.error("No auditors in auditing flow node {}", node.getId());
            return false;
        }
        List<String> userIdList = Lists.newArrayList();
        for (AuditorGroup group: groups) {
            String userIds = group.getUserIds();
            if (StringUtils.isBlank(userIds)) {
                continue;
            }
            userIdList.addAll(Splitter.on(",").splitToList(userIds));
        }
        if (CollectionUtils.isNotEmpty(userIdList)) { // every auditor has different urls to view data
            for (String userId: userIdList) {
                try {
                    String msg = auditingDataType.dataHandler().getAnnouncingMsg(userId, auditorRecordId, dataId);
                    WxWorkMessageUtil.sendWxWorkMsg(msg, userId);
                } catch (PmException e) {
                    logger.error("Announcing auditors error: {}", e);
                }
            }
//            String receivers = Joiner.on("|").join(userIdList);
//            try {
//                WxWorkMessageUtil.sendWxWorkMsg(msg, receivers);
//            } catch (PmException e) {
//                logger.error("Announcing auditors error: {}", e);
//                return false;
//            }
        }
        return true;
    }

    private static final String auditingCompletedMsgPattern = "%s,您提交业务单申请的已经审批完成，请及时处理。";
    @Async
    public boolean announceStarter(String starterUserId) {
        User user = userService.findByUserId(starterUserId);
        if (Objects.isNull(user)) {
            return false;
        }
        String username = user.getName();
        String msg = String.format(auditingCompletedMsgPattern, username);
        try {
            WxWorkMessageUtil.sendWxWorkMsg(msg, starterUserId);
        } catch (PmException e) {
            logger.error(e.getErrorMessage());
            return false;
        }
        return true;
    }

    public List<AuditingRecord> queryAuditableRecordsByAuditingFlowId(Long auditingFlowId) {
        AuditingRecord condition = new AuditingRecord();
        if (Objects.nonNull(auditingFlowId)) {
            condition.setAuditingFlowId(auditingFlowId);
            condition.setAuditingStatus(AuditingConstant.AuditingStatus.IN_PROGRESS.getStatus());
        }
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        return auditingRecordDao.findAll(Example.of(condition));
    }

}
