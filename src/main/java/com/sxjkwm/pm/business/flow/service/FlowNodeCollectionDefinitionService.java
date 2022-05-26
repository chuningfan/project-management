package com.sxjkwm.pm.business.flow.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.flow.dao.FlowNodeCollectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
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

    public static final String collectionTableNamePrefix = "coll_tb_";

    private final FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao;

    @Autowired
    public FlowNodeCollectionDefinitionService(FlowNodeCollectionDefinitionDao flowNodeCollectionDefinitionDao) {
        this.flowNodeCollectionDefinitionDao = flowNodeCollectionDefinitionDao;
    }


    @Transactional
    public List<FlowNodeCollectionDefinition> saveOrUpdate(Long flowNodeId, String collectionPropKey, List<FlowNodeCollectionDefDto> dtoList) throws SQLException {
        if (CollectionUtils.isNotEmpty(dtoList)) {
            FlowNodeCollectionDefinition condition = new FlowNodeCollectionDefinition();
            condition.setCollectionPropKey(collectionPropKey);
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
            for (FlowNodeCollectionDefDto dto: dtoList) {
                definition = new FlowNodeCollectionDefinition();
                definition.setCollectionPropKey(collectionPropKey);
                definition.setFlowNodeId(flowNodeId);
                definition.setIsDeleted(dto.getIsDeleted());
                definition.setHeaderIndex(dto.getHeaderIndex());
                definition.setHeaderKey(dto.getHeaderKey());
                definition.setHeaderName(dto.getHeaderName());
                if (Objects.nonNull(dto.getId()) && definition.getIsDeleted().intValue() == 0) {
                    definition.setId(dto.getId());
                    if (Objects.nonNull(existingMap)) {
                        FlowNodeCollectionDefinition originDef = existingMap.get(dto.getId());
                        if (Objects.nonNull(originDef) && !originDef.getCollectionPropKey().equals(dto.getCollectionPropKey())) {
                            String modifyStr = originDef.getCollectionPropKey() + "," + dto.getCollectionPropKey();
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
            String tableName = collectionTableNamePrefix + collectionPropKey;
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
                    builder.append("collection_prop_key       VARCHAR(255),");
                    builder.append("flow_node_id       BIGINT(20),");
                    builder.append("project_id       BIGINT(20),");
                    builder.append("created_by       VARCHAR(255),");
                    builder.append("created_at       BIGINT(20)");
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

}
