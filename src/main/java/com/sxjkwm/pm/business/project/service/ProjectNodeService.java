package com.sxjkwm.pm.business.project.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.flow.dao.FlowNodeCollectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.flow.service.FlowNodeCollectionDefinitionService;
import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodeDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodePropertyDao;
import com.sxjkwm.pm.business.project.dto.ProjectNodeDto;
import com.sxjkwm.pm.business.project.dto.ProjectNodePropertyDto;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.business.project.entity.ProjectNode;
import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import com.sxjkwm.pm.common.PropertyHandler;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.ContextUtil;
import com.sxjkwm.pm.util.DBUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;
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

    private final ProjectFileDao projectFileDao;

    private final FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao;

    @Autowired
    public ProjectNodeService(ProjectNodeDao projectNodeDao, ProjectNodePropertyDao projectNodePropertyDao, FlowNodeDefinitionDao flowNodeDefinitionDao, ProjectDao projectDao, ProjectFileDao projectFileDao, FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao) {
        this.projectNodeDao = projectNodeDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.projectDao = projectDao;
        this.projectFileDao = projectFileDao;
        this.flowNodeCollectionDefinitionDao = flowNodeCollectionDefinitionDao;
        handlerMap = ContextUtil.getBeansOfType(PropertyHandler.class);
    }

    @Transactional
    public ProjectNodeDto saveOrUpdate(ProjectNodeDto projectNodeDto) throws PmException, SQLException {
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
        if (Objects.isNull(project.getCurrentNodeId()) || project.getCurrentNodeId() < projectNodeDto.getFlowNodeId()) {
            project.setCurrentNodeId(projectNodeDto.getFlowNodeId());
            projectDao.save(project);
        }
        FlowNodeCollectionDefinition condition = new FlowNodeCollectionDefinition();
        condition.setFlowNodeId(projectNodeDto.getFlowNodeId());
        condition.setIsDeleted(0);
        List<FlowNodeCollectionDefinition> flowNodeCollectionDefinitions = flowNodeCollectionDefinitionDao.findAll(Example.of(condition), Sort.by(Sort.Direction.ASC, "headerIndex"));
        Map<String, List<FlowNodeCollectionDefinition>> flowNodeCollectionDefKeyMap = null;
        if (CollectionUtils.isNotEmpty(flowNodeCollectionDefinitions)) {
            flowNodeCollectionDefKeyMap = flowNodeCollectionDefinitions.stream().collect(Collectors.groupingBy(FlowNodeCollectionDefinition::getCollectionPropKey));
        }
        Long projectNodeId = projectNode.getId();
        List<Long> removeIds = Lists.newArrayList();
        List<ProjectNodeProperty> projectNodePropertyList = Lists.newArrayList();
        for (ProjectNodePropertyDto dto: propertyDtos) {
            String tableName = FlowNodeCollectionDefinitionService.collectionTableNamePrefix + dto.getPropertyKey();
            dto.setProjectId(projectNodeDto.getProjectId());
            String type = dto.getPropertyType();
            Constant.PropertyType propertyType = Constant.PropertyType.valueOf(type.toUpperCase(Locale.ROOT));
            List<Map<String, Object>> collectionData;
            if (propertyType == Constant.PropertyType.COLLECTION && CollectionUtils.isNotEmpty(collectionData = dto.getCollectionData()) && Objects.nonNull(flowNodeCollectionDefKeyMap)) {
                List<FlowNodeCollectionDefinition> definitions = flowNodeCollectionDefKeyMap.get(dto.getPropertyKey());
                if (CollectionUtils.isNotEmpty(definitions)) {
                    List<String> flowNodeCollectionDefKeys = definitions.stream().map(FlowNodeCollectionDefinition::getHeaderKey).collect(Collectors.toList());
                    StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + "(").append(Joiner.on(",").join(flowNodeCollectionDefKeys)).append(", collection_prop_key, flow_node_id, project_id) VALUES ");
                    for (Map<String, Object> dataMap : collectionData) {
                        Long dataId = (Long) dataMap.get("id");
                        if (Objects.nonNull(dataId)) {
                            removeIds.add(dataId);
                            dataMap.remove("id");
                        }
                        builder.append("(");
                        List<String> vals = Lists.newArrayList();
                        for (String columnName : flowNodeCollectionDefKeys) {
                            String val = (String) dataMap.get(columnName);
                            vals.add(val);
                        }
                        builder.append("'");
                        builder.append(Joiner.on("','").join(vals)).append("'");
                        builder.append(",'" + dto.getPropertyKey() + "'");
                        builder.append("," + projectNodeDto.getFlowNodeId());
                        builder.append("," + projectNodeDto.getProjectId());
                        builder.append("),");
                    }
                    if (CollectionUtils.isNotEmpty(removeIds)) {
                        DBUtil.executeSQL("DELETE FROM " + tableName + " WHERE ID IN (" + Joiner.on(",").join(removeIds) + ")");
                    }
                    DBUtil.executeSQL(builder.toString().substring(0, builder.toString().lastIndexOf(",")));
                }
            }
            ProjectNodeProperty projectNodeProperty = new ProjectNodeProperty();
            projectNodeProperty.setProjectId(projectNodeDto.getProjectId());
            dto.setProjectNodeId(projectNodeId);
            BeanUtils.copyProperties(dto, projectNodeProperty);
            projectNodeProperty.setProjectId(projectNodeDto.getProjectId());
            projectNodePropertyList.add(projectNodeProperty);
        }

        if (CollectionUtils.isNotEmpty(projectNodePropertyList)) {
            projectNodePropertyDao.saveAll(projectNodePropertyList);
        }
        return projectNodeDto;
    }

    public ProjectNodeDto getOne(Long projectId, Long flowNodeId) throws SQLException {

       ProjectNodeDto dto = new ProjectNodeDto();
        List<FlowNodeDefinition> flowNodeDefinitions = flowNodeDefinitionDao.getByFlowNodeId(flowNodeId);
        flowNodeDefinitions = flowNodeDefinitions.stream().filter(fnd -> fnd.getIsDeleted().intValue() == 0).collect(Collectors.toList());
        ProjectNode condition = new ProjectNode();
        condition.setProjectId(projectId);
        condition.setFlowNodeId(flowNodeId);
        condition.setIsDeleted(0);
        Example<ProjectNode> example = Example.of(condition);
        ProjectNode projectNode = projectNodeDao.findOne(example).orElse(null);
        if (Objects.isNull(projectNode)) {
            projectNode = new ProjectNode();
            projectNode.setProjectId(projectId);
            projectNode.setNodeStatus(0);
            projectNode.setFlowNodeId(flowNodeId);
            projectNode.setIsDeleted(0);
            projectNodeDao.save(projectNode);
        }
        dto.setProjectId(projectId);
        dto.setNodeStatus(projectNode.getNodeStatus());
        dto.setFlowNodeId(flowNodeId);
        dto.setId(projectNode.getId());
        List<ProjectNodePropertyDto> propertyDtos = Lists.newArrayList();
        ProjectNodeProperty propCondition = new ProjectNodeProperty();
        propCondition.setProjectId(projectId);
        propCondition.setProjectNodeId(projectNode.getId());
        List<ProjectNodeProperty> projectNodePropertyList = projectNodePropertyDao.findAll(Example.of(propCondition));
        Map<String, ProjectNodeProperty> propertyMap = null;
        if (CollectionUtils.isNotEmpty(projectNodePropertyList)) {
            propertyMap = projectNodePropertyList.stream().collect(Collectors.toMap(ProjectNodeProperty::getPropertyKey, prop -> prop, (k1, k2) -> k1));
        }
        ProjectNodePropertyDto projectNodePropertyDto;
        ProjectNodeProperty projectNodeProperty = null;
        FlowNodeCollectionDefinition collectionDefinitionCondition = new FlowNodeCollectionDefinition();
        collectionDefinitionCondition.setFlowNodeId(flowNodeId);
        collectionDefinitionCondition.setIsDeleted(0);
        List<FlowNodeCollectionDefinition> flowNodeCollectionDefinitions = flowNodeCollectionDefinitionDao.findAll(Example.of(collectionDefinitionCondition), Sort.by(Sort.Direction.ASC, "headerIndex"));
        Map<String, List<FlowNodeCollectionDefinition>> defMap = null;
        if (CollectionUtils.isNotEmpty(flowNodeCollectionDefinitions)) {
            defMap = flowNodeCollectionDefinitions.stream().collect(Collectors.groupingBy(FlowNodeCollectionDefinition::getCollectionPropKey));
        }
        for (FlowNodeDefinition definition : flowNodeDefinitions) {
            projectNodePropertyDto = new ProjectNodePropertyDto();
            String key = definition.getPropertyKey();
            projectNodePropertyDto.setPropertyKey(key);
            projectNodePropertyDto.setProjectId(projectId);
            projectNodePropertyDto.setProjectNodeId(projectNode.getId());
            projectNodePropertyDto.setPropertyType(definition.getPropertyType());
            projectNodePropertyDto.setPropertyIndex(definition.getPropertyIndex());
            projectNodePropertyDto.setPropertyName(definition.getPropertyName());
            projectNodePropertyDto.setCollectionPropertyHandler(definition.getCollectionPropertyHandler());
            if (Objects.nonNull(propertyMap)) {
                projectNodeProperty = propertyMap.get(key);
            }
            if (Objects.nonNull(projectNodeProperty)) {
                projectNodePropertyDto.setId(projectNodeProperty.getId());
                if ("FILE".equalsIgnoreCase(definition.getPropertyType())) {
                    String propVal =  projectNodeProperty.getPropertyValue();
                    if (StringUtils.isNotBlank(propVal)) {
                        Long fileId = Long.valueOf(propVal);
                        ProjectFile projectFile = projectFileDao.getOne(fileId);
                        projectNodePropertyDto.setFileName(projectFile.getFileName());
                    }
                    projectNodePropertyDto.setPropertyValue(propVal);
                } else {
                    projectNodePropertyDto.setPropertyValue(projectNodeProperty.getPropertyValue());
                }
            }
            if ("COLLECTION".equalsIgnoreCase(definition.getPropertyType()) && Objects.nonNull(defMap)) {
                List<FlowNodeCollectionDefinition> definitions = defMap.get(key);
                if (CollectionUtils.isNotEmpty(definitions)) {
                    List<String> flowNodeCollectionDefKeys = definitions.stream().map(FlowNodeCollectionDefinition::getHeaderKey).collect(Collectors.toList());
                    String tableName = FlowNodeCollectionDefinitionService.collectionTableNamePrefix + key;
                    StringBuilder builder = new StringBuilder("SELECT id,collection_prop_key, project_id, flow_node_id,");
                    builder.append(Joiner.on(",").join(flowNodeCollectionDefKeys)).append(" FROM ").append(tableName);
                    List<Map<String, Object>> dataList = DBUtil.query(builder.toString(), flowNodeCollectionDefKeys);
                    projectNodePropertyDto.setCollectionData(dataList);
                }
            }
            propertyDtos.add(projectNodePropertyDto);
        }
        if (CollectionUtils.isNotEmpty(propertyDtos)) {
            propertyDtos = propertyDtos.stream().sorted(Comparator.comparing(ProjectNodePropertyDto::getPropertyIndex)).collect(Collectors.toList());
        }
        dto.setPropertyDtos(propertyDtos);
        return dto;
    }

}
