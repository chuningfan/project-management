package com.sxjkwm.pm.business.material.dao;

import com.sxjkwm.pm.business.material.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/5/31 15:26
 */
public interface MaterialDao extends JpaRepository<Material, Long> {
}
