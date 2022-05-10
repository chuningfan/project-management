package com.sxjkwm.pm.business.finance.dao;

import com.sxjkwm.pm.business.finance.entity.OperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRecordDao extends JpaRepository<OperationRecord, Long> {
}
