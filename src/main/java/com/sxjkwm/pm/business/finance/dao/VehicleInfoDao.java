package com.sxjkwm.pm.business.finance.dao;

import com.sxjkwm.pm.business.finance.entity.VehicleInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleInfoDao extends JpaRepository<VehicleInfoEntity, Long> {
}
