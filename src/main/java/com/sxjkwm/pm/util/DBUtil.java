package com.sxjkwm.pm.util;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/5/25 8:47
 */
public class DBUtil {

    private static final String collectionTableNamePrefix = "coll_tb_";

    private static JdbcTemplate jdbcTemplate = null;

    public static void init(JdbcTemplate jdbcTemplate) {
        DBUtil.jdbcTemplate = jdbcTemplate;
    }

    public static boolean executeSQL(String sql) throws SQLException {
        jdbcTemplate.execute(sql);
        return true;
    }

    public static List<Map<String, Object>> query(String sql, List<String> otherColumnNames) throws SQLException {
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> dataList = Lists.newArrayList();
        Map<String, Object> dataMap;
        for (Map<String, Object> resultSet: resultList) {
            dataMap = Maps.newHashMap();
            Long id = MapUtil.getLong(resultSet, "id");
            dataMap.put("id", id);
            Long flowNodeId = MapUtil.getLong(resultSet, "flow_node_id");
            dataMap.put("flowNodeId", flowNodeId);
            Long projectId = MapUtil.getLong(resultSet, "project_id");
            dataMap.put("projectId", projectId);
            Long collectionPropDefId = MapUtil.getLong(resultSet, "collection_prop_def_id");
            dataMap.put("collectionPropDefId", collectionPropDefId);
            Long createdAt = MapUtil.getLong(resultSet, "created_at");
            dataMap.put("createdAt", createdAt);
            Long modifiedAt = MapUtil.getLong(resultSet, "modified_at");
            dataMap.put("modifiedAt", modifiedAt);
            String createdBy = MapUtil.getStr(resultSet, "created_by");
            dataMap.put("createdBy", createdBy);
            String modifiedBy = MapUtil.getStr(resultSet, "modified_by");
            dataMap.put("modifiedBy", modifiedBy);
            for (String column: otherColumnNames) {
                dataMap.put(column, MapUtil.getStr(resultSet, column));
            }
            dataList.add(dataMap);
        }
        return dataList;
    }

    public static String getTableName(Long flowId, Long flowNodeId, Long collPropDefId) {
        String tableName = collectionTableNamePrefix + flowId + "_" + flowNodeId + "_" + collPropDefId;
        return tableName;
    }

}
