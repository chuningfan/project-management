package com.sxjkwm.pm.business.flow.service;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.flow.dao.FlowNodeCollectionDefinitionDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;
import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionPropTypeDto;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.entity.FlowNodeCollectionDefinition;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.util.DBUtil;
import com.sxjkwm.pm.util.PinYinUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;
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
            condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
            condition.setCollectionPropertyDefId(collectionPropDefId);
            condition.setFlowNodeId(flowNodeId);
            List<FlowNodeCollectionDefinition> existingDefs = flowNodeCollectionDefinitionDao.findAll(Example.of(condition));
            Map<Long, FlowNodeCollectionDefinition> existingMap = null;
            if (CollectionUtils.isNotEmpty(existingDefs)) {
                existingMap = existingDefs.stream().collect(Collectors.toMap(FlowNodeCollectionDefinition::getId, d -> d));
            }
            List<FlowNodeCollectionDefinition> definitions = Lists.newArrayList();
            List<FlowNodeCollectionDefinition> newColumns = Lists.newArrayList();
            Map<String, FlowNodeCollectionDefinition> modifyColumns = Maps.newHashMap();
            List<FlowNodeCollectionDefinition> dropColumns = Lists.newArrayList();
            FlowNodeCollectionDefinition definition;
            for (FlowNodeCollectionDefDto dto: dtoList) {
                definition = new FlowNodeCollectionDefinition(dto);
                FlowNodeCollectionDefinition originDef = existingMap.get(definition.getId());
                String headerKey = definition.getHeaderKey();
                if (StringUtils.isBlank(headerKey) || !definition.getHeaderName().equals(originDef.getHeaderName())) {
                    headerKey = "col_" + flowNodeId + "_" + collectionPropDefId + "_" + PinYinUtil.convertChineseWordToPinYin(dto.getHeaderName(), "_");
                }
                definition.setHeaderKey(headerKey);
                if (Objects.nonNull(definition.getId())) { // 修改已有列
                    if (definition.isDeleted()) {
                        dropColumns.add(definition);
                    }
                    if (definition.isAvailable()) {
                        if (originDef.shouldBeModified(definition)) {
                            modifyColumns.put(originDef.getHeaderKey(), definition);
                        }
                    }
                } else {
                    newColumns.add(definition);  // 直接新增
                }
                definitions.add(definition);
            }
            definitions = flowNodeCollectionDefinitionDao.saveAll(definitions);
            // create table;
            String tableName = DBUtil.getTableName(flowNode.getFlowId(), flowNodeId, collectionPropDefId);
            Integer tableCount = flowNodeCollectionDefinitionDao.tryCreateTable(tableName);
            if (tableCount > 0) {
                // 表已存在
                if (!modifyColumns.isEmpty()) {
                    Set<Map.Entry<String, FlowNodeCollectionDefinition>> entrySet = modifyColumns.entrySet();
                    Iterator<Map.Entry<String, FlowNodeCollectionDefinition>> iterator = entrySet.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, FlowNodeCollectionDefinition> entry = iterator.next();
                        FlowNodeCollectionDefinition newDef = entry.getValue();
                        modifyColumn(tableName, entry.getKey(), newDef.getHeaderKey(), newDef.getPropertyType());
                    }
                }
                if (CollectionUtils.isNotEmpty(newColumns)) {
                    for (FlowNodeCollectionDefinition newColumn: newColumns) {
                        addColumn(tableName, newColumn.getHeaderKey(), newColumn.getPropertyType());
                    }
                }
                if (CollectionUtils.isNotEmpty(dropColumns)) {
                    for (FlowNodeCollectionDefinition dropColumn: dropColumns) {
                        dropColumn(tableName, dropColumn.getHeaderKey());
                    }
                }
            } else {
                if (CollectionUtils.isNotEmpty(newColumns)) {
                    StringBuilder builder = new StringBuilder("CREATE TABLE ");
                    builder.append(tableName).append(" (");
                    builder.append("id      BIGINT(20) primary key NOT NULL AUTO_INCREMENT,");
                    for (FlowNodeCollectionDefinition newColumn: newColumns) {
                        String propType = newColumn.getPropertyType();
                        propType = StringUtils.isBlank(propType) ? " VARCHAR(255)," : (" " + propType + ",");
                        builder.append(newColumn.getHeaderKey() + propType);
                    }
                    builder.append("collection_prop_def_id       BIGINT(20),");
                    builder.append("flow_node_id       BIGINT(20),");
                    builder.append("project_id       BIGINT(20),");
                    builder.append("created_by       VARCHAR(255),");
                    builder.append("created_at       BIGINT(20),");
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

    private void dropColumn(String tableName, String dropColumnName) throws SQLException {
        String alterSql = "ALTER TABLE " + tableName + " DROP COLUMN " + dropColumnName;
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

    public List<FlowNodeCollectionDefDto> findByFlowNodeIdAndCollectionDefId(Long flowNodeId, Long collectionDefId) {
        FlowNodeCollectionDefinition condition = new FlowNodeCollectionDefinition();
        condition.setIsDeleted(Constant.YesOrNo.NO.getValue());
        condition.setCollectionPropertyDefId(collectionDefId);
        condition.setFlowNodeId(flowNodeId);
        List<FlowNodeCollectionDefinition> existingDefs = flowNodeCollectionDefinitionDao.findAll(Example.of(condition));
        if (CollectionUtils.isEmpty(existingDefs)) {
            return Collections.emptyList();
        }
        List<FlowNodeCollectionDefDto> dataList = Lists.newArrayList();
        FlowNodeCollectionDefDto flowNodeCollectionDefDto;
        for (FlowNodeCollectionDefinition def: existingDefs) {
            flowNodeCollectionDefDto = new FlowNodeCollectionDefDto(def);
            dataList.add(flowNodeCollectionDefDto);
        }
        return dataList;
    }

    public Boolean sort(List<Long> nodeIds) {
        List<FlowNodeCollectionDefinition> collectionDefinitionList = flowNodeCollectionDefinitionDao.findByIds(nodeIds);
        if (CollectionUtils.isEmpty(collectionDefinitionList)) {
            return false;
        }
        Map<Long, FlowNodeCollectionDefinition> definitionMap = collectionDefinitionList.stream().collect(Collectors.toMap(FlowNodeCollectionDefinition::getId, fcd -> fcd, (k1, k2) -> k1));
        List<FlowNodeCollectionDefinition> saveList = Lists.newArrayList();
        FlowNodeCollectionDefinition definition;
        for (int i = 0; i < nodeIds.size(); i++) {
            Long id = nodeIds.get(i);
            definition = definitionMap.get(id);
            if (Objects.nonNull(definition)) {
                definition.setHeaderIndex(i);
                saveList.add(definition);
            }
        }
        if (CollectionUtils.isNotEmpty(saveList)) {
            flowNodeCollectionDefinitionDao.saveAll(saveList);
        }
        return true;
    }

    public List<FlowNodeCollectionPropTypeDto> getCollectionPropTypes() {
        List<FlowNodeCollectionPropTypeDto> dataList = Lists.newArrayList();
        FlowNodeCollectionPropTypeDto dto;
        for (Constant.PropertyType type: Constant.PropertyType.values()) {
            if (type.collectable()) {
                dto = new FlowNodeCollectionPropTypeDto(type.getLabel(), type.getValue(), type.getDbType());
                dataList.add(dto);
            }
        }
        return dataList;
    }

}
