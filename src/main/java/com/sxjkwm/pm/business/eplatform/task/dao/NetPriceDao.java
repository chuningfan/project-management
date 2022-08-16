package com.sxjkwm.pm.business.eplatform.task.dao;

import com.sxjkwm.pm.business.eplatform.task.entity.NetPriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/8/15 14:24
 */
public interface NetPriceDao extends JpaRepository<NetPriceRecord, Long> {
}
