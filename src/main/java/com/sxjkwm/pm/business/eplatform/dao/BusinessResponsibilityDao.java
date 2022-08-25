package com.sxjkwm.pm.business.eplatform.dao;

import com.sxjkwm.pm.business.eplatform.entity.BusinessResponsibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Vic.Chu
 * @date 2022/8/23 16:32
 */
public interface BusinessResponsibilityDao extends JpaRepository<BusinessResponsibility, Long> {

    @Query("SELECT br FROM BusinessResponsibility br WHERE br.ownerId = ?1")
    BusinessResponsibility findByOwnerId(String ownerId);

}
