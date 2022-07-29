package com.sxjkwm.pm.business.project.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.entity.Project;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProjectService {

    private final List<String> superRoles = Lists.newArrayList("admin");

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
        BeanUtils.copyProperties(projectDto, project, "createdBy", "createdAt");
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

    public Page<Project> queryMine(String userId, Integer projectStatus, String projectCode, String projectName, String requirePart, Integer pageNo, Integer pageSize) {
        if (pageNo <= 1) {
            pageNo = 0;
        } else {
            pageNo -= 1;
        }
        UserDataDto userDataDto = ContextHelper.getUserData();
        List<String> roleNames = userDataDto.getRoleNames();
        final AtomicBoolean useUserId = new AtomicBoolean(true);
        if (CollectionUtils.isNotEmpty(roleNames) && roleNames.stream().anyMatch(r -> superRoles.contains(r))) {
            useUserId.set(false);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Specification<Project> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            if (useUserId.get()) {
                expressions.add(criteriaBuilder.equal(root.get("ownerUserId").as(String.class), userId));
            }
            expressions.add(criteriaBuilder.equal(root.get("isDeleted").as(Integer.class), 0));
            if (Objects.nonNull(projectStatus)) {
                expressions.add(criteriaBuilder.equal(root.get("projectStatus").as(Integer.class), projectStatus));
            }
            if (StringUtils.isNotBlank(projectCode)) {
                expressions.add(criteriaBuilder.like(root.get("projectCode").as(String.class), "%" + projectCode + "%"));
            }
            if (StringUtils.isNotBlank(projectName)) {
                expressions.add(criteriaBuilder.like(root.get("projectName").as(String.class), "%" + projectName + "%"));
            }
            if (StringUtils.isNotBlank(requirePart)) {
                expressions.add(criteriaBuilder.like(root.get("requirePart").as(String.class), "%" + requirePart + "%"));
            }
            return predicate;
        };
        Page<Project> page = projectDao.findAll(specification, pageable);
        return page;
    }

}
