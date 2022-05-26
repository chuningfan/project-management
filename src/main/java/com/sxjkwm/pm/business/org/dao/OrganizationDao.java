package com.sxjkwm.pm.business.org.dao;

import com.sxjkwm.pm.business.org.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/5/24 18:22
 */
public interface OrganizationDao extends JpaRepository<Organization, Long> {
}
