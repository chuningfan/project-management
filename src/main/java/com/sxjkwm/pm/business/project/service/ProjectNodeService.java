package com.sxjkwm.pm.business.project.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
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
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/5/15 13:50
 */
@Service
public class ProjectNodeService {

    private final ProjectNodeDao projectNodeDao;

    private final ProjectNodePropertyDao projectNodePropertyDao;

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    private final ProjectDao projectDao;

    private final Map<String, PropertyHandler> handlerMap;

    @Autowired
    public ProjectNodeService(ProjectNodeDao projectNodeDao, ProjectNodePropertyDao projectNodePropertyDao, FlowNodeDefinitionDao flowNodeDefinitionDao, ProjectDao projectDao) {
        this.projectNodeDao = projectNodeDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.projectDao = projectDao;
        handlerMap = ContextUtil.getBeansOfType(PropertyHandler.class);
    }

    @Transactional
    public ProjectNodeDto saveOrUpdate(ProjectNodeDto projectNodeDto) throws PmException {
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

    public ProjectNodeDto getOne(Long id) throws PmException {
        ProjectNode projectNode = projectNodeDao.getOne(id);
        if (Objects.isNull(projectNode)) {
            throw new PmException(PmError.NO_DATA_FOUND, String.format("Cannot find project node by id = %d", id));
        }
        Long projectId = projectNode.getProjectId();
        ProjectNodeDto result = new ProjectNodeDto();
        result.setFlowNodeId(projectNode.getFlowNodeId());
        result.setId(projectNode.getId());
        result.setProjectId(projectNode.getProjectId());
        result.setNodeStatus(projectNode.getNodeStatus());
        List<FlowNodeDefinition> flowNodeDefinitions = flowNodeDefinitionDao.getByFlowNodeId(projectNode.getFlowNodeId());
        if (CollectionUtils.isEmpty(flowNodeDefinitions)) {
            throw new PmException(PmError.NO_DATA_FOUND, String.format("Cannot find project node definition by project_node_id = %d", id));
        }
        Map<String, FlowNodeDefinition> flowNodeDefinitionMap = flowNodeDefinitions.stream().collect(Collectors.toMap(FlowNodeDefinition::getPropertyKey, fnd -> fnd, (k1, k2) -> k1));
        ProjectNodeProperty condition = new ProjectNodeProperty();
        condition.setProjectNodeId(id);
        condition.setProjectId(projectNode.getProjectId());
        List<ProjectNodeProperty> projectNodePropertyList = projectNodePropertyDao.findAll(Example.of(condition));
        if (CollectionUtils.isEmpty(projectNodePropertyList)) {
            throw new PmException(PmError.NO_DATA_FOUND, String.format("Cannot find project node properties by project_node_id = %d", id));
        }
        List<ProjectNodePropertyDto> propertyDtos = Lists.newArrayList();
        result.setPropertyDtos(propertyDtos);
        ProjectNodePropertyDto dto;
        for (ProjectNodeProperty projectNodeProperty: projectNodePropertyList) {
            String propertyKey = projectNodeProperty.getPropertyKey();
            String propertyTypeString = projectNodeProperty.getPropertyType();
            Constant.PropertyType propertyType = Constant.PropertyType.valueOf(propertyTypeString.toUpperCase(Locale.ROOT));
            FlowNodeDefinition flowNodeDefinition = flowNodeDefinitionMap.get(propertyKey);
            dto = new ProjectNodePropertyDto();
            dto.setId(projectNodeProperty.getId());
            dto.setProjectId(projectId);
            dto.setProjectNodeId(id);
            dto.setPropertyIndex(flowNodeDefinition.getPropertyIndex());
            dto.setPropertyType(flowNodeDefinition.getPropertyType());
            dto.setPropertyName(flowNodeDefinition.getPropertyName());
            dto.setPropertyKey(propertyKey);
            if (propertyType == Constant.PropertyType.COLLECTION) {
                String handlerBeanName = flowNodeDefinition.getCollectionPropertyHandler();
                PropertyHandler propertyHandler = handlerMap.get(handlerBeanName);
                List<? extends BaseCollectionProperty> dataList = propertyHandler.query(projectId, id, propertyKey);
                dto.setCollectionData(dataList);
                dto.setCollectionPropertyHandler(handlerBeanName);
            } else {
                dto.setPropertyValue(projectNodeProperty.getPropertyValue());
            }
            propertyDtos.add(dto);
        }
        return result;
    }

    public ProjectNodeDto getOne(Long projectId, Long flowNodeId) {
        ProjectNodeDto dto = new ProjectNodeDto();
        List<FlowNodeDefinition> flowNodeDefinitions = flowNodeDefinitionDao.getByFlowNodeId(flowNodeId);
        flowNodeDefinitions = flowNodeDefinitions.stream().filter(fnd -> fnd.getIsDeleted().intValue() == 0).collect(Collectors.toList());
        ProjectNode condition = new ProjectNode();
        condition.setProjectId(projectId);
        condition.setFlowNodeId(flowNodeId);
        condition.setIsDeleted(0);
        Example<ProjectNode> example = Example.of(condition);
        ProjectNode projectNode = projectNodeDao.findOne(example).orElse(null);
        if (Objects.nonNull(projectNode)) {
            dto.setHasValue(true);
            dto.setProjectId(projectId);
            dto.setNodeStatus(projectNode.getNodeStatus());
            dto.setFlowNodeId(flowNodeId);
            dto.setId(projectNode.getId());
            List<ProjectNodePropertyDto> propertyDtos = Lists.newArrayList();
            dto.setPropertyDtos(propertyDtos);
            ProjectNodeProperty propCondition = new ProjectNodeProperty();
            propCondition.setProjectId(projectId);
            propCondition.setProjectNodeId(projectNode.getId());
            List<ProjectNodeProperty> projectNodePropertyList = projectNodePropertyDao.findAll(Example.of(propCondition));
            if (CollectionUtils.isNotEmpty(projectNodePropertyList)) {
                Map<String, ProjectNodeProperty> propertyMap = projectNodePropertyList.stream().collect(Collectors.toMap(ProjectNodeProperty::getPropertyKey, prop -> prop, (k1, k2) -> k1));
                ProjectNodePropertyDto projectNodePropertyDto;
                for (FlowNodeDefinition definition : flowNodeDefinitions) {
                    projectNodePropertyDto = new ProjectNodePropertyDto();
                    String key = definition.getPropertyKey();
                    projectNodePropertyDto.setPropertyKey(key);
                    projectNodePropertyDto.setPropertyType(definition.getPropertyType());
                    projectNodePropertyDto.setPropertyIndex(definition.getPropertyIndex());
                    projectNodePropertyDto.setPropertyName(definition.getPropertyName());
                    projectNodePropertyDto.setCollectionPropertyHandler(definition.getCollectionPropertyHandler());
                    ProjectNodeProperty projectNodeProperty = propertyMap.get(key);
                    if (Objects.nonNull(projectNodeProperty)) {
                        projectNodePropertyDto.setId(projectNodeProperty.getId());
                        if ("COLLECTION".equalsIgnoreCase(definition.getPropertyType())) {
                            String propertyHandler = definition.getCollectionPropertyHandler();
                            PropertyHandler handler = handlerMap.get(propertyHandler);
                            List dataList = handler.query(projectId, projectNode.getId(), key);
                            projectNodePropertyDto.setCollectionData(dataList);
                        } else {
                            projectNodePropertyDto.setPropertyValue(projectNodeProperty.getPropertyValue());
                        }
                    }
                    propertyDtos.add(projectNodePropertyDto);
                }
            }
        }
        return dto;
    }

//    @Transactional
//    public ProjectNodeDto update(ProjectNodeDto projectNodeDto) throws PmException {
//        Long id = projectNodeDto.getId();
//        ProjectNode projectNode = projectNodeDao.getOne(id);
//        String createdBy = projectNode.getCreatedBy();
//        Long createdAt = projectNode.getCreatedAt();
//        BeanUtils.copyProperties(projectNodeDto, projectNode);
//        projectNode.setCreatedAt(createdAt);
//        projectNode.setCreatedBy(createdBy);
//        projectNodeDao.save(projectNode);
//        List<ProjectNodePropertyDto> propertyDtos = projectNodeDto.getPropertyDtos();
//        if (CollectionUtils.isNotEmpty(propertyDtos)) {
//            List<ProjectNodeProperty> projectNodePropertyList = Lists.newArrayList();
//            for (ProjectNodePropertyDto dto: propertyDtos) {
//                dto.setProjectId(projectNodeDto.getProjectId());
//                dto.setProjectNodeId(id);
//                ProjectNodeProperty projectNodeProperty = new ProjectNodeProperty();
//                BeanUtils.copyProperties(dto, projectNodeProperty);
//                projectNodePropertyList.add(projectNodeProperty);
//                if (Constant.PropertyType.valueOf(dto.getPropertyType().toUpperCase(Locale.ROOT)) == Constant.PropertyType.COLLECTION) {
//                    List<? extends BaseCollectionProperty> dataList = dto.getCollectionData();
//                    String handlerBeanName = dto.getCollectionPropertyHandler();
//                    PropertyHandler propertyHandler = handlerMap.get(handlerBeanName);
//                    if (Objects.isNull(propertyHandler)) {
//                        throw new PmException(PmError.NO_SUCH_BEAN);
//                    }
//                    propertyHandler.update(dataList);
//                }
//            }
//            projectNodePropertyDao.saveAll(projectNodePropertyList);
//        }
//        return projectNodeDto;
//    }

}
