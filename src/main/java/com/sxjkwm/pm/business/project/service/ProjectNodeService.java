package com.sxjkwm.pm.business.project.service;

import cn.hutool.core.map.MapUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auditing.dao.AuditingRecordDao;
import com.sxjkwm.pm.auditing.entity.AuditingRecord;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.flow.dao.FlowDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeCollectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeSelectionDefinitionDto;
import com.sxjkwm.pm.business.flow.entity.*;
import com.sxjkwm.pm.business.flow.service.FlowNodeSelectionDefinitionService;
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
import java.util.Arrays;
import java.util.List;
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

    private final ProjectFileDao projectFileDao;

    private final FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao;

    private final FlowNodeSelectionDefinitionService flowNodeSelectionDefinitionService;

    private final AuditingRecordDao auditingRecordDao;

    private final FlowNodeDao flowNodeDao;

    private final FlowDao flowDao;

    @Autowired
    public ProjectNodeService(ProjectNodeDao projectNodeDao, ProjectNodePropertyDao projectNodePropertyDao, FlowNodeDefinitionDao flowNodeDefinitionDao, ProjectDao projectDao, ProjectFileDao projectFileDao, FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao, FlowNodeSelectionDefinitionService flowNodeSelectionDefinitionService, AuditingRecordDao auditingRecordDao, FlowNodeDao flowNodeDao, FlowDao flowDao) {
        this.projectNodeDao = projectNodeDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.projectDao = projectDao;
        this.projectFileDao = projectFileDao;
        this.flowNodeCollectionDefinitionDao = flowNodeCollectionDefinitionDao;
        this.flowNodeSelectionDefinitionService = flowNodeSelectionDefinitionService;
        this.auditingRecordDao = auditingRecordDao;
        this.flowNodeDao = flowNodeDao;
        this.flowDao = flowDao;
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
        boolean saveProject = false;
        if (Objects.isNull(project.getCurrentNodeId()) || project.getCurrentNodeId() < projectNodeDto.getFlowNodeId()) {
            project.setCurrentNodeId(projectNodeDto.getFlowNodeId());
            saveProject = true;
        }
        Integer projectStatus = projectNodeDto.getProjectStatus();
        if (Objects.nonNull(projectStatus)) {
            project.setProjectStatus(projectStatus);
            saveProject = true;
        }
        if (saveProject) {
            projectDao.save(project);
        }
        FlowNodeCollectionDefinition condition = new FlowNodeCollectionDefinition();
        condition.setFlowNodeId(projectNodeDto.getFlowNodeId());
        condition.setIsDeleted(0);
        List<FlowNodeCollectionDefinition> flowNodeCollectionDefinitions = flowNodeCollectionDefinitionDao.findAll(Example.of(condition), Sort.by(Sort.Direction.ASC, "headerIndex"));
        Map<Long, List<FlowNodeCollectionDefinition>> flowNodeCollectionDefKeyMap = null;
        if (CollectionUtils.isNotEmpty(flowNodeCollectionDefinitions)) {
            flowNodeCollectionDefKeyMap = flowNodeCollectionDefinitions.stream().collect(Collectors.groupingBy(FlowNodeCollectionDefinition::getCollectionPropertyDefId));
        }
        Long projectNodeId = projectNode.getId();
        List<ProjectNodeProperty> projectNodePropertyList = Lists.newArrayList();
        for (ProjectNodePropertyDto dto: propertyDtos) {
            String tableName = DBUtil.getTableName(project.getFlowId(), projectNode.getFlowNodeId(), dto.getFlowNodePropertyDefId());
            dto.setProjectId(projectNodeDto.getProjectId());
            Long propDefId = dto.getFlowNodePropertyDefId();
            if (Objects.nonNull(flowNodeCollectionDefKeyMap)) {
                List<FlowNodeCollectionDefinition> refCollectionDefs = flowNodeCollectionDefKeyMap.get(propDefId);
                List<Map<String, Object>> collectionData;
                if (Objects.nonNull(flowNodeCollectionDefKeyMap) && CollectionUtils.isNotEmpty(refCollectionDefs) && CollectionUtils.isNotEmpty(collectionData = dto.getCollectionData())) {
                    List<FlowNodeCollectionDefinition> definitions = flowNodeCollectionDefKeyMap.get(dto.getFlowNodePropertyDefId());
                    if (CollectionUtils.isNotEmpty(definitions)) {
                        List<String> flowNodeCollectionDefKeys = definitions.stream().map(FlowNodeCollectionDefinition::getHeaderKey).collect(Collectors.toList());
                        StringBuilder builder = new StringBuilder("INSERT INTO " + tableName + "(").append(Joiner.on(",").join(flowNodeCollectionDefKeys)).append(", collection_prop_def_id, flow_node_id, project_id, created_at, modified_at, created_by, modified_by) VALUES ");
                        Long nowTime = System.currentTimeMillis();
                        Long createdAt = nowTime;
                        Long modifiedAt = nowTime;
                        String currentUser = ContextHelper.getUserData().getWxUserId();
                        String modifiedBy = currentUser;
                        String createdBy = currentUser;
                        for (Map<String, Object> dataMap : collectionData) {
                            Long dataId = Objects.isNull(dataMap.get("id")) ? null : Long.valueOf(dataMap.get("id").toString()) ;
                            if (Objects.nonNull(dataId)) {
                                createdAt = MapUtil.get(dataMap, "createdAt", Long.class);
                                createdBy = MapUtil.get(dataMap, "createdBy", String.class);
                            }
                            builder.append("(");
                            List<String> vals = Lists.newArrayList();
                            for (String columnName : flowNodeCollectionDefKeys) {
                                String val = (String) dataMap.get(columnName);
                                vals.add(val);
                            }
                            builder.append("'");
                            builder.append(Joiner.on("','").join(vals)).append("'");
                            builder.append("," + dto.getFlowNodePropertyDefId());
                            builder.append("," + projectNodeDto.getFlowNodeId());
                            builder.append("," + projectNodeDto.getProjectId());
                            builder.append("," + createdAt);
                            builder.append("," + modifiedAt);
                            builder.append(",'" + createdBy + "'");
                            builder.append(",'" + modifiedBy + "'");
                            builder.append("),");
                        }
//                        if (CollectionUtils.isNotEmpty(removeIds)) {
                            DBUtil.executeSQL("DELETE FROM " + tableName + " WHERE flow_node_id = " + projectNodeDto.getFlowNodeId() + " AND project_id = " + projectNodeDto.getProjectId() + " AND collection_prop_def_id = " + propDefId);
//                        }
                        DBUtil.executeSQL(builder.substring(0, builder.toString().lastIndexOf(",")));
                    }
                }
            }
//            if (Objects.nonNull(flowNodeSelectinoDefKeyMap) && !flowNodeSelectinoDefKeyMap.isEmpty()) {
//                List<FlowNodeSelectionDefinitionDto> selectionDefinitionDtos = flowNodeSelectinoDefKeyMap.get(dto.getId());
//                if (CollectionUtils.isNotEmpty(selectionDefinitionDtos)) {
//
//                }
//            }
            if (Constant.PropertyType.FILE.name().equals(dto.getPropertyType())) {
                continue;
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
        Project project = projectDao.getOne(projectId);
        if (Objects.isNull(project)) {
            return null;
        }
        ProjectNodeDto dto = new ProjectNodeDto();
        List<FlowNodeDefinition> flowNodeDefinitions = flowNodeDefinitionDao.getByFlowNodeId(flowNodeId);
        if (CollectionUtils.isEmpty(flowNodeDefinitions)) {
            return null;
        }
        Long flowId = project.getFlowId();
        List<FlowNode> flowNodes = flowNodeDao.getAvailableNodes(flowId);
        if (flowNodes.get(flowNodes.size() - 1).getId().equals(flowNodeId)) {
            dto.setLastNode(true);
        }
        Map<Long, FlowNodeDefinition> collectionPropertyDefMap = flowNodeDefinitions.stream().filter(f -> f.getPropertyType().equals("COLLECTION")).collect(Collectors.toMap(FlowNodeDefinition::getId, f -> f, (k1, k2) -> k1));
        Map<Long, List<FlowNodeCollectionDefinition>> collDefMap = null;
        if (Objects.nonNull(collectionPropertyDefMap) && !collectionPropertyDefMap.isEmpty()) {
            List<FlowNodeCollectionDefinition> collectionDefinitionList = flowNodeCollectionDefinitionDao.findByPropDefIds(Lists.newArrayList(collectionPropertyDefMap.keySet()));
            if (CollectionUtils.isNotEmpty(collectionDefinitionList)) {
                collDefMap = collectionDefinitionList.stream().collect(Collectors.groupingBy(FlowNodeCollectionDefinition::getCollectionPropertyDefId));
            }
        }
        List<FlowNodeSelectionDefinition> selectionDefinitions = flowNodeSelectionDefinitionService.getByFlowNodeId(flowNodeId);
        Map<Long, List<FlowNodeSelectionDefinitionDto>> flowNodeSelectinoDefKeyMap = null;
        if (CollectionUtils.isNotEmpty(selectionDefinitions)) {
            List<Long> selectionDefIds = selectionDefinitions.stream().map(FlowNodeSelectionDefinition::getId).collect(Collectors.toList());
            flowNodeSelectinoDefKeyMap = flowNodeSelectionDefinitionService.getDefinitionsByDefIds(selectionDefIds);
        }
        Map<Long, FlowNodeDefinition> filePropertyDefMap = flowNodeDefinitions.stream().filter(f -> f.getPropertyType().equals("FILE")).collect(Collectors.toMap(FlowNodeDefinition::getId, f -> f, (k1, k2) -> k1));
        List<Long> flowNodePropDefIds = flowNodeDefinitions.stream().map(FlowNodeDefinition::getId).collect(Collectors.toList());
        List<ProjectNodeProperty> propertyList = projectNodePropertyDao.findByPropDefIdsAndProjectId(flowNodePropDefIds, projectId);
        Map<Long, ProjectNodeProperty> defPropMap = null;
        if (CollectionUtils.isNotEmpty(propertyList)) {
            defPropMap = propertyList.stream().collect(Collectors.toMap(ProjectNodeProperty::getFlowNodePropertyDefId, p -> p, (k1, k2) -> k1));
        }
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
        dto.setPropertyDtos(propertyDtos);
        ProjectNodePropertyDto propertyDto;
        ProjectNodeProperty projectNodeProperty = null;
        for (FlowNodeDefinition flowNodeDefinition: flowNodeDefinitions) {
            propertyDto = new ProjectNodePropertyDto();
            if (Objects.nonNull(defPropMap)) {
                projectNodeProperty = defPropMap.get(flowNodeDefinition.getId());
                if (Objects.nonNull(projectNodeProperty)) {
                    BeanUtils.copyProperties(projectNodeProperty, propertyDto);
                    propertyDto.setId(projectNodeProperty.getId());
                }
            }
            propertyDto.setPropertyName(flowNodeDefinition.getPropertyName());
            propertyDto.setPropertyIndex(flowNodeDefinition.getPropertyIndex());
            propertyDto.setFlowNodePropertyDefId(flowNodeDefinition.getId());
            propertyDto.setPropertyKey(flowNodeDefinition.getPropertyKey());
            propertyDto.setProjectNodeId(projectNode.getId());
            propertyDto.setProjectId(projectId);
            String propType = flowNodeDefinition.getPropertyType();
            propertyDto.setPropertyType(propType);
            if ("COLLECTION".equals(propType) && Objects.nonNull(collDefMap)) {
                List<FlowNodeCollectionDefinition> flowNodeCollectionDefinitions = collDefMap.get(flowNodeDefinition.getId());
                if (Objects.nonNull(flowNodeCollectionDefinitions)) {
                    List<FlowNodeCollectionDefDto> flowNodeCollectionDefDtos = Lists.newArrayList();
                    FlowNodeCollectionDefDto flowNodeCollectionDefDto;
                    for (FlowNodeCollectionDefinition collectionDefinition: flowNodeCollectionDefinitions) {
                        flowNodeCollectionDefDto = new FlowNodeCollectionDefDto();
                        BeanUtils.copyProperties(collectionDefinition, flowNodeCollectionDefDto);
                        flowNodeCollectionDefDtos.add(flowNodeCollectionDefDto);
                    }
                    propertyDto.setFlowNodeCollectionDefDtos(flowNodeCollectionDefDtos);
                    List<String> flowNodeCollectionDefKeys = flowNodeCollectionDefDtos.stream().map(FlowNodeCollectionDefDto::getHeaderKey).collect(Collectors.toList());
                    String tableName = DBUtil.getTableName(project.getFlowId(), flowNodeId, flowNodeDefinition.getId());
                    Integer flag = flowNodeCollectionDefinitionDao.tryCreateTable(tableName);
                    if (flag == 1) {
                        StringBuilder builder = new StringBuilder("SELECT id,collection_prop_def_id, project_id, flow_node_id, created_at, created_by, modified_at, modified_by,");
                        builder.append(Joiner.on(",").join(flowNodeCollectionDefKeys)).append(" FROM ").append("project_management." + tableName);
                        builder.append(" WHERE project_id = ").append(projectId).append(" AND flow_node_id = ").append(flowNodeId);
                        List<Map<String, Object>> dataList = DBUtil.query(builder.toString(), flowNodeCollectionDefKeys);
                        propertyDto.setCollectionData(dataList);
                    }
                }
            } else if ("FILE".equals(propType) && Objects.nonNull(filePropertyDefMap)) {
                if (Objects.nonNull(projectNodeProperty)) {
                    String propVal = projectNodeProperty.getPropertyValue();
                    if (StringUtils.isNotBlank(propVal)) {
                        Long fileId = Long.valueOf(propVal);
                        ProjectFile projectFile = projectFileDao.getOne(fileId);
                        propertyDto.setFileName(projectFile.getFileName());
                        propertyDto.setPatternFileCategory(flowNodeDefinition.getPatternFileCategory());
                    }
                    propertyDto.setPropertyValue(propVal);
                }
            } else if ("CHECKBOX".equals(propType) || "RADIO".equals(propType)) {
                if  (Objects.nonNull(flowNodeSelectinoDefKeyMap)) {
                    List<FlowNodeSelectionDefinitionDto> selectionDefinitionDtos = flowNodeSelectinoDefKeyMap.get(flowNodeDefinition.getId());
                    if (CollectionUtils.isNotEmpty(selectionDefinitionDtos)) {
                        propertyDto.setFlowNodeSelectionDtos(selectionDefinitionDtos);
                    }
                    String val = projectNodeProperty.getPropertyValue();
                    propertyDto.setPropertyValue(val);
                    if (StringUtils.isNotBlank(val)) {
                        if ("CHECKBOX".equals(propType)) {
                            List<String> vals = Lists.newArrayList(Arrays.asList(val.split(",")));
                            for (FlowNodeSelectionDefinitionDto definitionDto: selectionDefinitionDtos) {
                                if (vals.contains(definitionDto.getSelectionValue())) {
                                    definitionDto.setSelected(true);
                                }
                            }
                        } else if ("RADIO".equals(propType)) {
                            FlowNodeSelectionDefinitionDto target = selectionDefinitionDtos.stream().filter(sd -> sd.getSelectionValue().equals(val)).findFirst().orElse(null);
                            if (Objects.nonNull(target)) {
                                target.setSelected(true);
                            }
                        }
                    }
                }
            } else {
                if (Objects.nonNull(projectNodeProperty)) {
                    propertyDto.setPropertyValue(projectNodeProperty.getPropertyValue());
                }
            }
            propertyDtos.add(propertyDto);
        }
        FlowNode flowNode = flowNodeDao.getOne(flowNodeId);
        if (Objects.nonNull(flowNode)) {
            Long auditingFlowId = flowNode.getAuditingFlowId();
            if (Objects.nonNull(auditingFlowId)) {
                Flow flow = flowDao.getOne(flowId);
                Long dataId = projectId;
                Integer auditingDataType = flow.getDataType();
                if (Objects.isNull(auditingDataType)) {
                    auditingDataType = 1;
                }
                AuditingRecord AuditingRecordCondition = new AuditingRecord();
                AuditingRecordCondition.setAuditingFlowId(auditingFlowId);
                AuditingRecordCondition.setIsDeleted(0);
                AuditingRecordCondition.setBusinessFlowNodeId(flowNodeId);
                AuditingRecordCondition.setDataType(auditingDataType);
                AuditingRecordCondition.setDataId(dataId);
                AuditingRecord auditingRecord = auditingRecordDao.findOne(Example.of(AuditingRecordCondition)).orElse(null);
                if (Objects.nonNull(auditingRecord)) {
                    dto.setAuditingStatus(auditingRecord.getAuditingStatus());
                }
            }
        }
        return dto;
    }


    public boolean deleteDataById(Long flowId, Long flowNodeId, Long flowNodeDefinitionId, Long dataId) {
        String tableName = DBUtil.getTableName(flowId, flowNodeId, flowNodeDefinitionId);
        try {
            DBUtil.executeSQL("DELETE FROM " + tableName + " WHERE id = " + dataId);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
