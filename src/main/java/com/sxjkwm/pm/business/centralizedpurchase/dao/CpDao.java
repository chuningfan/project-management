package com.sxjkwm.pm.business.centralizedpurchase.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/4 16:05
 */
@Repository
public class CpDao {

    @Autowired
    @Qualifier("cpJdbcTemplate")
    private JdbcTemplate cpJdbcTemplate;

    public List<Map<String, Object>> queryList(String querySql) {
        return cpJdbcTemplate.queryForList(querySql);
    }

    public Map<String, Object> findOne(String querySql) {
        return cpJdbcTemplate.queryForMap(querySql);
    }

    public void execute(String sql) {
        cpJdbcTemplate.execute(sql);
    }

}
