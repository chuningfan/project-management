package com.sxjkwm.pm.auditing.service;

import com.sxjkwm.pm.auditing.dao.AuditingRecordDao;
import com.sxjkwm.pm.auditing.entity.AuditingRecord;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/6/27 10:16
 */
@Service
public class AuditingRecordService {

    private final AuditingRecordDao auditingRecordDao;

    @Autowired
    public AuditingRecordService(AuditingRecordDao auditingRecordDao) {
        this.auditingRecordDao = auditingRecordDao;
    }

    public String getAuditingDataJSONString(Long auditingRecordId) throws PmException {
        AuditingRecord auditingRecord = auditingRecordDao.getOne(auditingRecordId);
        if (Objects.isNull(auditingRecord)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        return auditingRecord.getAuditingData();
    }

}
