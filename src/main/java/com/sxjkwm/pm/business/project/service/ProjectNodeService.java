package com.sxjkwm.pm.business.project.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodeDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodePropertyDao;
import com.sxjkwm.pm.business.project.dto.ProjectNodeDto;
import com.sxjkwm.pm.business.project.dto.ProjectNodePropertyDto;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.business.project.entity.ProjectNode;
import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import com.sxjkwm.pm.common.BaseCollectionProperty;
import com.sxjkwm.pm.common.PropertyHandler;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.ContextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:50
 */
@Service
public class ProjectNodeService {

    private final ProjectNodeDao projectNodeDao;

    private final ProjectNodePropertyDao projectNodePropertyDao;

    private final ProjectDao projectDao;

    private final Map<String, PropertyHandler> handlerMap;

    @Autowired
    public ProjectNodeService(ProjectNodeDao projectNodeDao, ProjectNodePropertyDao projectNodePropertyDao, ProjectDao projectDao) {
        this.projectNodeDao = projectNodeDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.projectDao = projectDao;
        handlerMap = ContextUtil.getBeansOfType(PropertyHandler.class);
    }

    @Transactional
    public ProjectNodeDto save(ProjectNodeDto projectNodeDto) throws PmException {
        List<ProjectNodePropertyDto> propertyDtos = projectNodeDto.getPropertyDtos();
        if (CollectionUtils.isEmpty(propertyDtos)) {
            return null;
        }
        Long id = projectNodeDto.getId();
        ProjectNode projectNode = new ProjectNode();
        if (Objects.nonNull(id)) {
            projectNode = projectNodeDao.getOne(id);
        }
        projectNode.setNodeStatus(projectNodeDto.getNodeStatus());
        projectNode.setProjectId(projectNodeDto.getProjectId());
        projectNode.setFlowNodeId(projectNodeDto.getFlowNodeId());
        projectNodeDao.save(projectNode);
        Project project = projectDao.getOne(projectNodeDto.getProjectId());
        if (Objects.isNull(project.getCurrentNodeId()) || project.getCurrentNodeId() < projectNode.getId()) {
            project.setCurrentNodeId(projectNode.getId());
            projectDao.save(project);
        }
        Long projectNodeId = projectNode.getId();
        List<ProjectNodeProperty> projectNodePropertyList = Lists.newArrayList();
        for (ProjectNodePropertyDto dto: propertyDtos) {
            String type = dto.getPropertyType();
            Constant.PropertyType propertyType = Constant.PropertyType.valueOf(type.toUpperCase(Locale.ROOT));
            List<? extends BaseCollectionProperty> collectionData;
            if (propertyType == Constant.PropertyType.COLLECTION && CollectionUtils.isNotEmpty(collectionData = dto.getCollectionData())) {
                String handlerBeanName = dto.getCollectionPropertyHandler();
                PropertyHandler propertyHandler = handlerMap.get(handlerBeanName);
                if (Objects.isNull(propertyHandler)) {
                    throw new PmException(PmError.NO_SUCH_BEAN);
                }
                propertyHandler.save(collectionData);
            }
            ProjectNodeProperty projectNodeProperty = new ProjectNodeProperty();
            dto.setProjectNodeId(projectNodeId);
            BeanUtils.copyProperties(dto, projectNodeProperty);
            projectNodePropertyList.add(projectNodeProperty);
        }
        if (CollectionUtils.isNotEmpty(projectNodePropertyList)) {
            projectNodePropertyDao.saveAll(projectNodePropertyList);
        }

        return projectNodeDto;
    }

    @Transactional
    public ProjectNodeDto update(ProjectNodeDto projectNodeDto) throws PmException {
        Long id = projectNodeDto.getId();
        ProjectNode projectNode = projectNodeDao.getOne(id);
        String createdBy = projectNode.getCreatedBy();
        Long createdAt = projectNode.getCreatedAt();
        if (Objects.isNull(projectNode)) {
            return null;
        }
        BeanUtils.copyProperties(projectNodeDto, projectNode);
        projectNode.setCreatedAt(createdAt);
        projectNode.setCreatedBy(createdBy);
        projectNodeDao.save(projectNode);
        List<ProjectNodePropertyDto> propertyDtos = projectNodeDto.getPropertyDtos();
        if (CollectionUtils.isNotEmpty(propertyDtos)) {
            List<ProjectNodeProperty> projectNodePropertyList = Lists.newArrayList();
            for (ProjectNodePropertyDto dto: propertyDtos) {
                dto.setProjectId(projectNodeDto.getProjectId());
                dto.setProjectNodeId(id);
                ProjectNodeProperty projectNodeProperty = new ProjectNodeProperty();
                BeanUtils.copyProperties(dto, projectNodeProperty);
                projectNodePropertyList.add(projectNodeProperty);
                if (Constant.PropertyType.valueOf(dto.getPropertyType().toUpperCase(Locale.ROOT)) == Constant.PropertyType.COLLECTION) {
                    List<? extends BaseCollectionProperty> dataList = dto.getCollectionData();
                    String handlerBeanName = dto.getCollectionPropertyHandler();
                    PropertyHandler propertyHandler = handlerMap.get(handlerBeanName);
                    if (Objects.isNull(propertyHandler)) {
                        throw new PmException(PmError.NO_SUCH_BEAN);
                    }
                    propertyHandler.update(dataList);
                }
            }
            projectNodePropertyDao.saveAll(projectNodePropertyList);
        }
        return projectNodeDto;
    }

}
