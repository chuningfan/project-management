package com.sxjkwm.pm.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.ProxyConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/5/25 8:47
 */
public class DBUtil {

    private static final String collectionTableNamePrefix = "coll_tb_";

    public static boolean executeSQL(String sql) throws SQLException {
        HikariDataSource ds = ContextUtil.getBean(HikariDataSource.class);
        ProxyConnection connection = (ProxyConnection) ds.getConnection();
        Statement statement = connection.createStatement();
        boolean res = statement.execute(sql);
        statement.close();
        return res;
    }

    public static List<Map<String, Object>> query(String sql, List<String> otherColumnNames) throws SQLException {
        HikariDataSource ds = ContextUtil.getBean(HikariDataSource.class);
        ProxyConnection connection = (ProxyConnection) ds.getConnection();
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        List<Map<String, Object>> dataList = Lists.newArrayList();
        Map<String, Object> dataMap;
        while (resultSet.next()) {
            dataMap = Maps.newHashMap();
            Long id = resultSet.getLong("id");
            dataMap.put("id", id);
            Long flowNodeId = resultSet.getLong("flow_node_id");
            dataMap.put("flowNodeId", flowNodeId);
            Long projectId = resultSet.getLong("project_id");
            dataMap.put("projectId", projectId);
            Long collectionPropDefId = resultSet.getLong("collection_prop_def_id");
            dataMap.put("collectionPropDefId", collectionPropDefId);
            Long createdAt = resultSet.getLong("created_at");
            dataMap.put("createdAt", createdAt);
            Long modifiedAt = resultSet.getLong("modified_at");
            dataMap.put("modifiedAt", modifiedAt);
            String createdBy = resultSet.getString("created_by");
            dataMap.put("createdBy", createdBy);
            String modifiedBy = resultSet.getString("modified_by");
            dataMap.put("modifiedBy", modifiedBy);
            for (String column: otherColumnNames) {
                dataMap.put(column, resultSet.getString(column));
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
