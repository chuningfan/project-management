package com.sxjkwm.pm.business.project.dao;

import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:44
 */
public interface ProjectNodePropertyDao extends JpaRepository<ProjectNodeProperty, Long> {

    @Query("SELECT pp FROM ProjectNodeProperty pp WHERE pp.flowNodePropertyDefId IN (?1) AND project_id = ?2")
    public List<ProjectNodeProperty> findByPropDefIdsAndProjectId(List<Long> flowNodePropertyDefIds, Long projectId);

}
