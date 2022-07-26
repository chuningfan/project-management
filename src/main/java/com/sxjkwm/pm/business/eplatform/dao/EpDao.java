package com.sxjkwm.pm.business.eplatform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/22 14:26
 */
@Repository
public class EpDao {

    @Autowired
    @Qualifier("epJdbcTemplate")
    private JdbcTemplate epJdbcTemplate;

    public List<Map<String, Object>> queryList(String querySql) {
        return epJdbcTemplate.queryForList(querySql);
    }

    public Map<String, Object> findOne(String querySql) {
        return epJdbcTemplate.queryForMap(querySql);
    }

//    public void execute(String sql) {
//        epJdbcTemplate.execute(sql);
//    }

}
