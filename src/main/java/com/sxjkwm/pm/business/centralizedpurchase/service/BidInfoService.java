package com.sxjkwm.pm.business.centralizedpurchase.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.centralizedpurchase.dao.CpDao;
import com.sxjkwm.pm.business.centralizedpurchase.dto.BidInfoDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/7/14 15:32
 */
@Service
public class BidInfoService {

    private final CpDao cpDao;

    @Autowired
    public BidInfoService(CpDao cpDao) {
        this.cpDao = cpDao;
    }

    public List<BidInfoDto> queryBidInfoByProjectCode(String projectCode) {
        List<BidInfoDto> returnList = Lists.newArrayList();
        StringBuilder sqlBuilder = new StringBuilder("SELECT tendercode as projectCode, tendername as projectName, " +
                "projectname as requirePart, materialclassname as materialClassName, materialname as materialName, " +
                "specification as materialSpecification, brand, unitprice as unitPrice, planquantity as planQuantity, unit, " +
                "suppliername as supplierName, orgname as requirePartParentOrgName, totalprice as totalPrice, taxrate as taxRate, " +
                "tax as taxAmount, cost as amountWithoutTax FROM gsrm7_sxjk_prod_sourcing.proc_resultmaterial WHERE tendercode = '").append(projectCode).append("'");
        List<Map<String, Object>> dataList = cpDao.queryList(sqlBuilder.toString());
        if (CollectionUtils.isNotEmpty(dataList)) {
            BidInfoDto dto;
            for (Map<String, Object> rowData: dataList) {
                dto = new BidInfoDto();
                dto.setProjectCode(MapUtils.getString(rowData, "projectCode"));
                dto.setProjectName(MapUtils.getString(rowData, "projectName"));
                dto.setRequirePart(MapUtils.getString(rowData, "requirePart"));
                dto.setMaterialClassName(MapUtils.getString(rowData, "materialClassName"));
                dto.setMaterialName(MapUtils.getString(rowData, "materialName"));
                dto.setMaterialSpecification(MapUtils.getString(rowData, "materialSpecification"));
                dto.setBrand(MapUtils.getString(rowData, "brand"));
                dto.setUnitPrice((BigDecimal)MapUtils.getObject(rowData, "unitPrice"));
                dto.setPlanQuantity((BigDecimal)MapUtils.getObject(rowData, "planQuantity"));
                dto.setUnit(MapUtils.getString(rowData, "unit"));
                dto.setSupplierName(MapUtils.getString(rowData, "supplierName"));
                dto.setRequirePartParentOrgName(MapUtils.getString(rowData, "requirePartParentOrgName"));
                dto.setTotalPrice((BigDecimal)MapUtils.getObject(rowData, "totalPrice"));
                dto.setTaxRate((BigDecimal)MapUtils.getObject(rowData, "taxRate"));
                dto.setTaxAmount((BigDecimal)MapUtils.getObject(rowData, "taxAmount"));
                dto.setAmountWithoutTax((BigDecimal)MapUtils.getObject(rowData, "amountWithoutTax"));
                returnList.add(dto);
            }
        }
        return returnList;
    }

}
