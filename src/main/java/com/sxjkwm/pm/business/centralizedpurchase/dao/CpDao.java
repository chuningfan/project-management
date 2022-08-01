package com.sxjkwm.pm.business.centralizedpurchase.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CpDao.class);

    @Autowired
    @Qualifier("cpJdbcTemplate")
    private JdbcTemplate cpJdbcTemplate;

    public List<Map<String, Object>> queryList(String querySql) {
        return cpJdbcTemplate.queryForList(querySql);
    }

    public Map<String, Object> findOne(String querySql) {
        try {
            return cpJdbcTemplate.queryForMap(querySql);
        } catch (Exception e) {
            logger.error("Query by SQL error: {}", e);
            return null;
        }
    }

    public void execute(String sql) {
        cpJdbcTemplate.execute(sql);
    }

}
