package com.sxjkwm.pm.auth.dao;

import com.sxjkwm.pm.auth.entity.ExternalEnterprise;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/7/15 8:36
 */
public interface ExternalEnterpriseDao extends JpaRepository<ExternalEnterprise, Long> {
}
