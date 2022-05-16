package com.sxjkwm.pm.business.project.service;

import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.entity.Project;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class ProjectService {

    private final ProjectDao projectDao;

    @Autowired
    public ProjectService(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Transactional
    public ProjectDto saveOrUpdate(ProjectDto projectDto) {
        Long id = projectDto.getId();
        Project project = new Project();
        if (Objects.nonNull(id)) {
            project = projectDao.getOne(id);
        }
        BeanUtils.copyProperties(projectDto, project);
        projectDao.save(project);
        projectDto.setId(project.getId());
        return projectDto;
    }

    public ProjectDto getId(Long id) {
        Project project = projectDao.getOne(id);
        if (Objects.isNull(project)) {
            return null;
        }
        ProjectDto projectDto = new ProjectDto();
        BeanUtils.copyProperties(project, projectDto);
        return projectDto;
    }

}
