package com.sxjkwm.pm.business.centralizedpurchase.service;

import cn.hutool.core.map.MapUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.business.centralizedpurchase.dao.CpDao;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
        String sql = "SELECT taskcode AS projectCode, taskname AS projectName, orgname AS requirePart, assignername AS deptName, budget FROM gsrm7_sxjk_prod_plan.`pl_task` WHERE taskcode = '" + taskCode + "'";
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

    /**
     * 已定标数据 生成合同
     * @param projectCode
     * @return
     */
    public List<Map<String, Object>> findMaterialOfProject(String projectCode) {
        String itemListSql = "SELECT DISTINCT it.itemname, mc.materialclassname, pm.materialname, pm.specification, pm.unit, pm.planquantity, pm.remark, pm.brand, " +
                " pr.unitprice, pr.totalprice, pr.taxrate, pr.tax, pr.cost, pr.suppliername, pr.winningdate, pr.suppliercontactperson, pr.suppliermobile " +
                " FROM gsrm7_sxjk_prod_sourcing.`proc_tender` pt " +
                " INNER JOIN gsrm7_sxjk_prod_sourcing.`proc_item` it ON pt.tenderid = it.tenderid " +
                " INNER JOIN gsrm7_sxjk_prod_sourcing.`proc_material` pm ON pm.tenderid = it.tenderid  AND it.itemcode = pm.type " +
                " inner join gsrm7_sxjk_prod_sourcing.`proc_resultmaterial` pr on pr.tenderid = pm.tenderid and pr.materialcode = pm.materialcode" +
                " INNER JOIN gsrm7_sxjk_prod_base.`mat_class` mc ON mc.materialclasscode = SUBSTRING(pm.materialcode, 1, 5) " +
                " WHERE pt.tendercode = '" + projectCode + "' ";
        List<Map<String, Object>> dataList = cpDao.queryList(itemListSql);
        return dataList;
    }

    /**
     * 询价文件数据 生成询价文件
     * @param projectCode
     */
    public List<Map<String, Object>> findMaterialOfTask(String projectCode) throws PmException {
        String planIdSql = "SELECT planid FROM gsrm7_sxjk_prod_plan.`pl_task` WHERE taskcode = '" + projectCode + "'";
        Map<String, Object> result = cpDao.findOne(planIdSql);
        if (Objects.isNull(result) || result.isEmpty()) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        String planIdsString = MapUtils.getString(result, "planid");
        List<String> planIds = Splitter.on(",").splitToList(planIdsString);
        String planIdCondition = "";
        if (planIds.size() > 1) {
            planIdCondition = "'" + Joiner.on("','").join(planIds) + "'";
        } else {
            planIdCondition = "'" + planIds.get(0) + "'";
        }
        String itemSql = "SELECT DISTINCT ppm.planname AS itemname, NULL AS materialclassname, ppm.materialname, ppm.specification, ppm.unit, ppm.planquantity, ppm.remark, ppm.brand " +
                " FROM gsrm7_sxjk_prod_plan.`pl_pur_material`ppm " +
                " LEFT JOIN gsrm7_sxjk_prod_plan.`pl_pur_construct` ppc ON ppc.planid = ppm.planid " +
                " WHERE ppm.planid IN (" + planIdCondition + ") ";
        return cpDao.queryList(itemSql);
    }

}
