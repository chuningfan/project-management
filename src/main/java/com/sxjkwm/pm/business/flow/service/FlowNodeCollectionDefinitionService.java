package com.sxjkwm.pm.business.flow.service;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.flow.dao.FlowNodeCollectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.util.DBUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/5/25 7:36
 */
@Service
public class FlowNodeCollectionDefinitionService {

    private final FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao;

    private final FlowNodeDao flowNodeDao;

    @Autowired
    public FlowNodeCollectionDefinitionService(FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao, FlowNodeDao flowNodeDao) {
        this.flowNodeCollectionDefinitionDao = flowNodeCollectionDefinitionDao;
        this.flowNodeDao = flowNodeDao;
    }

    @Transactional
    public List<FlowNodeCollectionDefinition> saveOrUpdate(Long flowNodeId, Long collectionPropDefId, List<FlowNodeCollectionDefDto> dtoList) throws SQLException {
        if (CollectionUtils.isNotEmpty(dtoList)) {
            FlowNode flowNode = flowNodeDao.getOne(flowNodeId);
            FlowNodeCollectionDefinition condition = new FlowNodeCollectionDefinition();
            condition.setCollectionPropertyDefId(collectionPropDefId);
            condition.setFlowNodeId(flowNodeId);
            List<FlowNodeCollectionDefinition> existingDefs = flowNodeCollectionDefinitionDao.findAll(Example.of(condition));
            Map<Long, FlowNodeCollectionDefinition> existingMap = null;
            if (CollectionUtils.isNotEmpty(existingDefs)) {
                existingMap = existingDefs.stream().collect(Collectors.toMap(FlowNodeCollectionDefinition::getId, d -> d));
            }
            List<FlowNodeCollectionDefinition> definitions = Lists.newArrayList();
            List<String> newColumns = Lists.newArrayList();
            List<String> modifyColumns = Lists.newArrayList();
            FlowNodeCollectionDefinition definition;
            int colNo = 1;
            for (FlowNodeCollectionDefDto dto: dtoList) {
                definition = new FlowNodeCollectionDefinition();
                definition.setCollectionPropertyDefId(collectionPropDefId);
                definition.setFlowNodeId(flowNodeId);
                definition.setIsDeleted(dto.getIsDeleted());
                definition.setHeaderIndex(dto.getHeaderIndex());
                String headerKey = dto.getHeaderKey();
                if (StringUtils.isBlank(headerKey)) {
                    headerKey = "col_" + flowNodeId + "_" + collectionPropDefId + "_" + (colNo ++);
                }
                definition.setHeaderKey(headerKey);
                definition.setHeaderName(dto.getHeaderName());
                if (Objects.nonNull(dto.getId()) && definition.getIsDeleted().intValue() == 0) {
                    definition.setId(dto.getId());
                    if (Objects.nonNull(existingMap)) {
                        FlowNodeCollectionDefinition originDef = existingMap.get(dto.getId());
                        if (Objects.nonNull(originDef) && !originDef.getCollectionPropertyDefId().equals(dto.getCollectionPropertyDefId())) {
                            String modifyStr = originDef.getCollectionPropertyDefId() + "," + dto.getCollectionPropertyDefId();
                            modifyColumns.add(modifyStr);
                        }
                    }
                } else {
                    newColumns.add(definition.getHeaderKey());
                }
                definitions.add(definition);
            }
            definitions = flowNodeCollectionDefinitionDao.saveAll(definitions);
            // create table;
            String tableName = DBUtil.getTableName(flowNode.getFlowId(), flowNodeId, collectionPropDefId);
            Integer tableCount = flowNodeCollectionDefinitionDao.tryCreateTable(tableName);
            if (tableCount > 0) {
                // 表已存在
                if (CollectionUtils.isNotEmpty(modifyColumns)) {
                    for (String mod: modifyColumns) {
                        String originColumn = mod.split(",")[0];
                        String newColumn = mod.split(",")[1];
                        modifyColumn(tableName, originColumn, newColumn, null);
                    }
                }
                if (CollectionUtils.isNotEmpty(newColumns)) {
                    for (String newColumn: newColumns) {
                        addColumn(tableName, newColumn, null);
                    }
                }
            } else {
                if (CollectionUtils.isNotEmpty(newColumns)) {
                    StringBuilder builder = new StringBuilder("CREATE TABLE ");
                    builder.append(tableName).append(" (");
                    builder.append("id      BIGINT(20) primary key NOT NULL AUTO_INCREMENT,");
                    for (String newColumn: newColumns) {
                        builder.append(newColumn + " VARCHAR(255),");
                    }
                    builder.append("collection_prop_def_id       BIGINT(20),");
                    builder.append("flow_node_id       BIGINT(20),");
                    builder.append("project_id       BIGINT(20),");
                    builder.append("created_by       VARCHAR(255),");
                    builder.append("created_at       BIGINT(20)");
                    builder.append("modified_by       VARCHAR(255),");
                    builder.append("modified_at       BIGINT(20)");
                    builder.append(" ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
                    DBUtil.executeSQL(builder.toString());
                }
            }
            return definitions;
        }
        return Collections.emptyList();
    }

    private void modifyColumn(String tableName, String originColumnName, String newColumnName, String def) throws SQLException {
        if (StringUtils.isBlank(def)) {
            def = " VARCHAR(255)";
        }
        String alterSql = "ALTER TABLE " + tableName + " CHANGE " + originColumnName + " " + newColumnName + " " + def;
        DBUtil.executeSQL(alterSql);
    }

    private void addColumn(String tableName, String newColumnName, String def) throws SQLException {
        if (StringUtils.isBlank(def)) {
            def = " VARCHAR(255)";
        }
        String alterSql = "ALTER TABLE " + tableName + " ADD " + newColumnName + " " + def;
        DBUtil.executeSQL(alterSql);
    }

    public List<FlowNodeCollectionDefDto> getFlowNodeCollectionDefDotsByPropDefIds(List<Long> propDefIds) {
        List<FlowNodeCollectionDefinition> collectionDefinitionList = flowNodeCollectionDefinitionDao.findByPropDefIds(propDefIds);
        if (CollectionUtils.isNotEmpty(collectionDefinitionList)) {
            List<FlowNodeCollectionDefDto> dtoList = Lists.newArrayList();
            for (FlowNodeCollectionDefinition definition: collectionDefinitionList) {
                dtoList.add(new FlowNodeCollectionDefDto(definition));
            }
            return dtoList;
        }
        return Collections.emptyList();
    }

}
