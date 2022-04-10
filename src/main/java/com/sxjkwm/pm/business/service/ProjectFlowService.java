package com.sxjkwm.pm.business.service;

import com.sxjkwm.pm.business.dao.ProjectFlowDao;
import com.sxjkwm.pm.business.entity.ProjectFlow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectFlowService {

    private final ProjectFlowDao projectFlowDao;

    @Autowired
    public ProjectFlowService(ProjectFlowDao projectFlowDao) {
        this.projectFlowDao = projectFlowDao;
    }

    public ProjectFlow createFlow(ProjectFlow projectFlow) {
        return projectFlowDao.save(projectFlow);
    }

    public ProjectFlow updateFlow(ProjectFlow projectFlow) {
        Long id = projectFlow.getId();
        ProjectFlow source = projectFlowDao.getOne(id);
        source.setFlowName(projectFlow.getFlowName());
        source.setIsDeleted(projectFlow.getIsDeleted());
        return source;
    }

    public ProjectFlow getFlow(Long id) {
        return projectFlowDao.getOne(id);
    }

    public List<ProjectFlow> getFlows(String flowName) {
        if (StringUtils.isBlank(flowName)) {
            return projectFlowDao.findAll();
        }
        ProjectFlow projectFlow = new ProjectFlow();
        projectFlow.setFlowName(flowName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("flowName", ExampleMatcher.GenericPropertyMatchers.contains());
        return projectFlowDao.findAll(Example.of(projectFlow, matcher));
    }

}
