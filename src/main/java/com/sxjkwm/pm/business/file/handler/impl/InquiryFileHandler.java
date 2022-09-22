package com.sxjkwm.pm.business.file.handler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.business.centralizedpurchase.dao.CpDao;
import com.sxjkwm.pm.business.centralizedpurchase.dto.Item;
import com.sxjkwm.pm.business.centralizedpurchase.service.ProjectInfoService;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.file.handler.replacement.ReplacementType;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.thirdparty.wxwork.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class InquiryFileHandler implements PatternFileHandler {

    private final ProjectService projectService;

    private final UserService userService;

    private final ProjectInfoService projectInfoService;

    @Autowired
    public InquiryFileHandler(ProjectService projectService, UserService userService, ProjectInfoService projectInfoService) {
        this.projectService = projectService;
        this.userService = userService;
        this.projectInfoService = projectInfoService;
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
        List<Map<String, Object>> dataList = projectInfoService.findMaterialOfTask(projectDto.getProjectCode());
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

    @Override
    public boolean isSingleFile() {
        return true;
    }

}
