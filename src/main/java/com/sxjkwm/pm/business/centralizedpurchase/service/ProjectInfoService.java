package com.sxjkwm.pm.business.centralizedpurchase.service;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.centralizedpurchase.dao.CpDao;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Vic.Chu
 * @date 2022/7/4 16:19
 */
@Service
public class ProjectInfoService {

    private final CpDao cpDao;

    @Autowired
    public ProjectInfoService(CpDao cpDao) {
        this.cpDao = cpDao;
    }

    public ProjectDto findProjectInfoByTaskCode(String taskCode) {
        String sql = "SELECT tendercode as projectCode, tendername as projectName, projectnamae as requirePart, employeeorgname as deptName, budget, deadline, procstatename as inquiryStatus FROM gsrm7_sxjk_prod_sourcing.`proc_tender` WHERE tendercode = '" + taskCode + "'";
        Map<String, Object> dataMap = cpDao.findOne(sql);
        if (Objects.isNull(dataMap)) {
            return null;
        }
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectName(MapUtil.getStr(dataMap, "projectName"));
        projectDto.setProjectCode(MapUtil.getStr(dataMap, "projectCode"));
        projectDto.setRequirePart(MapUtil.getStr(dataMap, "requirePart"));
        projectDto.setDeptName(MapUtil.getStr(dataMap, "deptName"));
        Date deadline = MapUtil.getDate(dataMap,"deadLine");
        if (Objects.nonNull(deadline)) {
            projectDto.setDeadLine(deadline.getTime());
        }
        projectDto.setBudget(new BigDecimal(MapUtil.getStr(dataMap, "budget")));
        return projectDto;
    }

    public List<String> queryProjectCodes() {
        String mobile = ContextHelper.getUserData().getMobile();
        if (StringUtils.isBlank(mobile)) {
            return Collections.emptyList();
        }
        String employeeIdSql = "SELECT ee.`employeeid` FROM gsrm7_sxjk_prod_enterprise.auth_account aa INNER JOIN gsrm7_sxjk_prod_enterprise.`en_employee` ee ON ee.`systemuserid` = aa.systemuserid WHERE aa.mobile = '" + mobile + "'";
        Map<String, Object> dataMap = cpDao.findOne(employeeIdSql);
        if (Objects.isNull(dataMap) || dataMap.isEmpty()) {
            return Collections.emptyList();
        }
        String employeeId = MapUtil.getStr(dataMap, "employeeid");
        if (StringUtils.isBlank(employeeId)) {
            return Collections.emptyList();
        }
        String codesSql = "SELECT tendercode FROM gsrm7_sxjk_prod_sourcing.`proc_tender` WHERE employeeid = '" + employeeId + "' AND ";
        List<Map<String, Object>> codeList = cpDao.queryList(codesSql);
        if (CollectionUtils.isNotEmpty(codeList)) {
            List<String> result = Lists.newArrayList();
            for (Map<String, Object> data: codeList) {
                result.add(MapUtil.getStr(data, "tendercode"));
            }
            return result;
        }
        return Collections.emptyList();
    }

}
