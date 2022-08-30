package com.sxjkwm.pm.business.eplatform.dao;

import com.sxjkwm.pm.business.eplatform.dto.InvoiceDto;
import com.sxjkwm.pm.business.eplatform.dto.InvoiceInfoDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
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

    // 根据发票号和发票申请单查询开票金额
    public List<InvoiceInfoDto> queryInvoiceCount(String querySql,InvoiceDto invoiceDto) {
        String countSql = null;
        if (StringUtils.isEmpty(invoiceDto.getApplyNumber())) {
            countSql = "GROUP BY a3.order_no";
        } else {
            countSql = "AND a2.apply_number = ? GROUP BY a3.order_no";
        }
        return epJdbcTemplate.query(querySql + countSql, new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        if (StringUtils.isNotEmpty(invoiceDto.getApplyNumber())) {
                            preparedStatement.setString(1,invoiceDto.getApplyNumber());
                        }
                    }
                },
                BeanPropertyRowMapper.newInstance(InvoiceInfoDto.class));

    }

    // 根据发票号和发票申请单查询开票金额
    public List<InvoiceInfoDto> queryInvoiceList(String querySql, InvoiceDto invoiceDto) {
        String sql = null;


        if (StringUtils.isEmpty(invoiceDto.getApplyNumber())) {
             sql = "GROUP BY a3.order_no limit ?,?";
        } else {
             sql = "AND a2.apply_number = ? GROUP BY a3.order_no limit ?,?";
        }

            return epJdbcTemplate.query(querySql+sql, new PreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement) throws SQLException {
                            if (StringUtils.isEmpty(invoiceDto.getApplyNumber())) {
                                preparedStatement.setInt(1, invoiceDto.getPageNum() - 1);
                                preparedStatement.setInt(2, invoiceDto.getPageSize());
                            }else {
                                preparedStatement.setString(1, invoiceDto.getApplyNumber());
                                preparedStatement.setInt(2, invoiceDto.getPageNum() - 1);
                                preparedStatement.setInt(3, invoiceDto.getPageSize());
                            }
                        }
                    },
                    BeanPropertyRowMapper.newInstance(InvoiceInfoDto.class));
        }

}
