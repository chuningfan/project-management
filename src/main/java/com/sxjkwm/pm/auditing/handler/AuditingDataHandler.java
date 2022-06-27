package com.sxjkwm.pm.auditing.handler;

import com.sxjkwm.pm.auditing.dto.AuditingDataDto;
import com.sxjkwm.pm.exception.PmException;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/6/8 9:58
 */
public interface AuditingDataHandler<T> {

    List<AuditingDataDto> readData(Long dataId, Long businessFlowNodeId) throws PmException;

    T getData(Long dataId);

    String getAnnouncingMsg(String auditorUserId, Long auditingRecordId, Long dataId);

}
