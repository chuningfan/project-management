package com.sxjkwm.pm.auditing.handler.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auditing.dto.AuditingDataDto;
import com.sxjkwm.pm.auditing.handler.AuditingDataHandler;
import com.sxjkwm.pm.auth.dao.UserDao;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.business.flow.dao.FlowNodeCollectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodeDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodePropertyDao;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.AuditingUrlUtil;
import com.sxjkwm.pm.util.DBUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/6/8 14:18
 */
@Component
public class ProjectAuditingDataHandler implements AuditingDataHandler<Project> {

    private final ProjectDao projectDao;

    private final ProjectNodeDao projectNodeDao;

    private final ProjectNodePropertyDao projectNodePropertyDao;

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    private final FlowNodeDao flowNodeDao;

    private final FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao;

    private final UserDao userDao;

    @Autowired
    public ProjectAuditingDataHandler(ProjectDao projectDao, ProjectNodeDao projectNodeDao, ProjectNodePropertyDao projectNodePropertyDao, FlowNodeDefinitionDao flowNodeDefinitionDao, FlowNodeDao flowNodeDao, FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.projectNodeDao = projectNodeDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
        this.flowNodeDao = flowNodeDao;
        this.flowNodeCollectionDefinitionDao = flowNodeCollectionDefinitionDao;
        this.userDao = userDao;
    }

    @Override
    public List<AuditingDataDto> readData(Long dataId, Long businessFlowNodeId) throws PmException { // projectId
        FlowNode flowNode = flowNodeDao.getOne(businessFlowNodeId);
        if (Objects.isNull(flowNode)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        Integer index = flowNode.getNodeIndex();
        List<FlowNode> flowNodes = flowNodeDao.getEqualAndLessThanIndex(flowNode.getFlowId(), index);
        List<Long> flowNodeIds = flowNodes.stream().map(FlowNode::getId).collect(Collectors.toList());
        List<FlowNodeDefinition> auditableDefs = flowNodeDefinitionDao.auditableFlowNodeDefinitions(flowNodeIds);
        if (CollectionUtils.isNotEmpty(auditableDefs)) {
            Map<Long, List<FlowNodeCollectionDefinition>> collectionDefMap = null;
            if (auditableDefs.stream().anyMatch(fnd -> fnd.getPropertyType().equals("COLLECTION"))) {
                List<Long> collectionPropDefIds = auditableDefs.stream().filter(fnd -> fnd.getPropertyType().equals("COLLECTION")).map(FlowNodeDefinition::getId).collect(Collectors.toList());
                List<FlowNodeCollectionDefinition> flowNodeCollectionDefinitions = flowNodeCollectionDefinitionDao.findByPropDefIds(collectionPropDefIds);
                if (CollectionUtils.isNotEmpty(flowNodeCollectionDefinitions)) {
                    collectionDefMap = flowNodeCollectionDefinitions.stream().collect(Collectors.groupingBy(FlowNodeCollectionDefinition::getCollectionPropertyDefId));
                }
            }
            List<Long> defIds = auditableDefs.stream().map(FlowNodeDefinition::getId).collect(Collectors.toList());
            Map<Long, FlowNodeDefinition> defMap = auditableDefs.stream().collect(Collectors.toMap(FlowNodeDefinition::getId, fnd -> fnd, (k1, k2) -> k1));
            List<ProjectNodeProperty> projectNodePropertyList = projectNodePropertyDao.findByPropDefIdsAndProjectId(defIds, dataId);
            if (CollectionUtils.isNotEmpty(projectNodePropertyList)) {
                List<AuditingDataDto> dataList = Lists.newArrayList();
                Long defId;
                FlowNodeDefinition def;
                AuditingDataDto dto;
                for (ProjectNodeProperty projectNodeProperty: projectNodePropertyList) {
                    defId = projectNodeProperty.getFlowNodePropertyDefId();
                    def = defMap.get(defId);
                    dto = new AuditingDataDto();
                    dto.setPropertyName(def.getPropertyName());
                    dto.setPropertyType(def.getPropertyType());
                    dto.setPropertyIndex(def.getPropertyIndex());
                    dto.setPropertyValue(projectNodeProperty.getPropertyValue());
                    if (Constant.PropertyType.valueOf(def.getPropertyType()) == Constant.PropertyType.COLLECTION && Objects.nonNull(collectionDefMap)) {
                        List<FlowNodeCollectionDefinition> collectionDefinitionList = collectionDefMap.get(defId);
                        if (CollectionUtils.isNotEmpty(collectionDefinitionList)) {
                            List<FlowNodeCollectionDefDto> collectionDefDtos = Lists.newArrayList();
                            for (FlowNodeCollectionDefinition definition: collectionDefinitionList) {
                                collectionDefDtos.add(new FlowNodeCollectionDefDto(definition));
                            }
                            dto.setFlowNodeCollectionDefDtos(collectionDefDtos);
                            String tableName = DBUtil.getTableName(flowNode.getFlowId(), businessFlowNodeId, defId);
                            dto.setDataList(queryData(collectionDefDtos.stream().map(FlowNodeCollectionDefDto::getHeaderKey).collect(Collectors.toList()), dataId, tableName, businessFlowNodeId));
                        }
                    }
                    dataList.add(dto);
                }
                return dataList;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public Project getData(Long dataId) {
        return projectDao.getOne(dataId);
    }

    private static final String msgPattern = "%s您好，现有%s负责的%s项目相关数据请您审批。点击<a href=\"%s\">这里</a>查看详情";

    @Override
    public String getAnnouncingMsg(String auditorUserId, Long auditingRecordId, Long dataId) {
        User auditor = userDao.findUserByWxUserId(auditorUserId);
        Project project = getData(dataId);
        String projectName = project.getProjectName();
        String userId = project.getOwnerUserId();
        User user = userDao.findUserByWxUserId(userId);
        String username = user.getName();
        String redirectUrl = AuditingUrlUtil.generateUrl(auditorUserId, auditingRecordId);
        return String.format(msgPattern, auditor.getName(), username, projectName, redirectUrl);
    }

    private List<Map<String, Object>> queryData(List<String> flowNodeCollectionDefKeys, Long projectId, String tableName, Long flowNodeId) {
        StringBuilder builder = new StringBuilder("SELECT id,collection_prop_def_id, project_id, flow_node_id, created_at, created_by, modified_at, modified_by,");
        builder.append(Joiner.on(",").join(flowNodeCollectionDefKeys)).append(" FROM ").append(tableName);
        builder.append(" WHERE project_id = ").append(projectId).append(" AND flow_node_id = ").append(flowNodeId);
        try {
            List<Map<String, Object>> dataList = DBUtil.query(builder.toString(), flowNodeCollectionDefKeys);
            return dataList;
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

}
