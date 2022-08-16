package com.sxjkwm.pm.business.eplatform.task.dao;

import com.sxjkwm.pm.business.eplatform.task.entity.TaskConfig;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Vic.Chu
 * @date 2022/8/15 14:27
 */
public interface TaskConfigDao extends JpaRepository<TaskConfig, Long> {
}
