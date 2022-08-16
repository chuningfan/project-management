package com.sxjkwm.pm.business.eplatform.task.dao;

import com.sxjkwm.pm.business.eplatform.task.entity.PriceChangingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/8/15 15:04
 */
public interface PriceChangingDao extends JpaRepository<PriceChangingRecord, Long> {
}
