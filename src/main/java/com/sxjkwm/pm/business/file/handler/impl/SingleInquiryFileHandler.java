package com.sxjkwm.pm.business.file.handler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.business.centralizedpurchase.dao.CpDao;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.file.handler.replacement.ReplacementType;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.wxwork.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单一来源询价文件
 * @author Vic.Chu
 * @date 2022/7/8 17:44
 */
@Component
public class SingleInquiryFileHandler implements PatternFileHandler {

    private static final String itemListSql = "SELECT DISTINCT it.itemname, pm.materialname, pm.specification, pm.unit, pm.planquantity, pm.remark, pm.brand " +
            " FROM gsrm7_sxjk_prod_sourcing.`proc_tender` pt " +
            " INNER JOIN gsrm7_sxjk_prod_sourcing.`proc_item` it ON pt.tenderid = it.tenderid " +
            " INNER JOIN gsrm7_sxjk_prod_sourcing.`proc_material` pm ON pm.tenderid = it.tenderid  AND it.itemcode = pm.type " +
            " WHERE pt.tendercode = '%s' ";

    private final ProjectService projectService;

    private final UserService userService;

    private final CpDao cpDao;

    @Autowired
    public SingleInquiryFileHandler(ProjectService projectService, UserService userService, CpDao cpDao) {
        this.projectService = projectService;
        this.userService = userService;
        this.cpDao = cpDao;
    }

    @Override
    public List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) throws PmException {
        List<BaseReplacement> replacements = Lists.newArrayList();
        ProjectDto projectDto = projectService.getById(dataId);
        if (Objects.isNull(projectDto)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        Long projectTime = projectDto.getProjectTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh时mm分");
        String projectTimeStr = "";
        if (Objects.nonNull(projectTime)) {
            Date date = Date.from(Instant.ofEpochMilli(projectTime));
            projectTimeStr = simpleDateFormat.format(date);
        }
        replacements.add(BaseReplacement.of("projectName", projectDto.getProjectName(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("projectCode", projectDto.getProjectCode(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("projectTime", projectTimeStr, ReplacementType.STRING));
        replacements.add(BaseReplacement.of("projectDescription", projectDto.getDescription(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("requirePart", projectDto.getRequirePart(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("purchaseScope", projectDto.getPurchaseScope(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("goodsReceiveAddress", projectDto.getGoodsReceiveAddress(), ReplacementType.STRING));
        String goodsReceiveTimeStr = "";
        Long goodsReceiveTime = projectDto.getGoodsReceiveTime();
        if (Objects.nonNull(goodsReceiveTime)) {
            Date date = Date.from(Instant.ofEpochMilli(goodsReceiveTime));
            goodsReceiveTimeStr = simpleDateFormat.format(date);
        }
        replacements.add(BaseReplacement.of("supplyPeriod", goodsReceiveTimeStr.split(" ")[0], ReplacementType.STRING));
        String deadlineStr = "";
        Long deadline = projectDto.getDeadLine();
        if (Objects.nonNull(deadline)) {
            Date date = Date.from(Instant.ofEpochMilli(deadline));
            deadlineStr = simpleDateFormat.format(date);
        }
        replacements.add(BaseReplacement.of("deadline", deadlineStr, ReplacementType.STRING));
        String ownerUserId = projectDto.getOwnerUserId();
        User owner = userService.findByUserId(ownerUserId);
        String ownerName = ContextHelper.getUserData().getUsername();
        if (Objects.isNull(owner)) {
            ownerName = owner.getName();
        }
        replacements.add(BaseReplacement.of("ownerName", ownerName, ReplacementType.STRING));
        replacements.add(BaseReplacement.of("paymentType", projectDto.getPaymentType(), ReplacementType.STRING));
        // query goods list
        String projectCode = projectDto.getProjectCode();
        String sql = String.format(itemListSql, projectCode);
        List<Map<String, Object>> dataList = cpDao.queryList(sql);
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<Item> items = Lists.newArrayList();
            for (Map<String, Object> dataMap: dataList) {
                items.add(new Item(dataMap));
            }
            Map<String, List<Item>> itemMap = items.stream().collect(Collectors.groupingBy(Item::getItemName));
            Set<Map.Entry<String, List<Item>>> itemMapEntrySet = itemMap.entrySet();
            Iterator<Map.Entry<String, List<Item>>> iterator = itemMapEntrySet.iterator();
            if (itemMap.size() > 1) { // 多标包
                LinkedHashMap<String, List<LinkedHashMap<String, String>>> tables = Maps.newLinkedHashMap();
                int count = 1;
                while (iterator.hasNext()) {
                    Map.Entry<String, List<Item>> entry = iterator.next();
                    String itemName = entry.getKey();
                    List<Item> materialList = entry.getValue();
                    List<LinkedHashMap<String, String>> rowDataList = Lists.newArrayList();
                    for (Item item: materialList) {
                        LinkedHashMap<String, String> row = Maps.newLinkedHashMap();
                        row.put("货物名称", item.getMaterialName());
                        row.put("型号", item.getSpecification());
                        row.put("品牌/产地", item.getBrand());
                        row.put("单位", item.getUnit());
                        row.put("数量", item.getQuantity());
                        rowDataList.add(row);
                    }
                    tables.put("标包" + (count ++) + " " + itemName, rowDataList);
                }
                replacements.add(BaseReplacement.of("goodsList", tables, ReplacementType.TABLE_WITH_MERGED_FIRST_COLUMN, "黑体", 12));
            } else {  // 单标包
                List<List<LinkedHashMap<String, String>>> tables = Lists.newArrayList();
                Map.Entry<String, List<Item>> entry = iterator.next();
                List<Item> materialList = entry.getValue();
                List<LinkedHashMap<String, String>> rowDataList = Lists.newArrayList();
                for (Item item: materialList) {
                    LinkedHashMap<String, String> row = Maps.newLinkedHashMap();
                    row.put("货物名称", item.getMaterialName());
                    row.put("型号", item.getSpecification());
                    row.put("品牌/产地", item.getBrand());
                    row.put("单位", item.getUnit());
                    row.put("数量", item.getQuantity());
                    rowDataList.add(row);
                }
                tables.add(rowDataList);
                replacements.add(BaseReplacement.of("goodsList", tables, ReplacementType.TABLE, "黑体", 12));
            }
        }
        return replacements;
    }

    private static class Item implements Serializable {
        private String itemName;

        private String materialName;

        private String specification;

        private String unit;

        private String quantity;

        private String brand;

        private String remark;

        public Item(Map<String, Object> dataMap) {
            this.itemName = MapUtils.getString(dataMap, "itemname");
            this.materialName = MapUtils.getString(dataMap, "materialname");
            this.specification = MapUtils.getString(dataMap, "specification");
            this.unit = MapUtils.getString(dataMap, "unit");
            this.quantity = MapUtils.getNumber(dataMap, "planquantity").toString();
            this.brand = MapUtils.getString(dataMap, "brand");
            this.remark = MapUtils.getString(dataMap, "remark");
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String getSpecification() {
            return specification;
        }

        public void setSpecification(String specification) {
            this.specification = specification;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
