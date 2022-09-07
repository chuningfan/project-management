package com.sxjkwm.pm.business.eplatform.dao;

import com.sxjkwm.pm.business.eplatform.dto.InvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.InvoiceInfoDto;
import com.sxjkwm.pm.business.eplatform.entity.InvoiceInfoEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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



    public List<Integer> queryInvoiceCount(String sql,Object[] params){
      return   epJdbcTemplate.queryForList(sql, params, Integer.class);
    }

    public List query(String sql, Object[] params, RowMapper rowMapper){
        return   epJdbcTemplate.query(sql, params, rowMapper);
    }

    public List<InvoiceInfoEntity> query(String sql, RowMapper rowMapper){
        return   epJdbcTemplate.query(sql, rowMapper);
    }

}
