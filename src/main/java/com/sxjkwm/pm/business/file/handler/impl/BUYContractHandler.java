package com.sxjkwm.pm.business.file.handler.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import com.sxjkwm.pm.util.RMBChange;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vic.Chu
 * @date 2022/8/17 8:44
 */
@Component
public class BUYContractHandler implements PatternFileHandler {

    private final ProjectService projectService;

    private final UserService userService;

    private final ProjectInfoService projectInfoService;

    @Autowired
    public BUYContractHandler(ProjectService projectService, UserService userService, ProjectInfoService projectInfoService) {
        this.projectService = projectService;
        this.userService = userService;
        this.projectInfoService = projectInfoService;
    }

    @Override
    public List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) throws PmException {
        return null;
    }

    @Override
    public boolean isSingleFile() {
        return false;
    }

    @Override
    public List<List<BaseReplacement>> captureDataCollection(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) throws PmException {
        List<List<BaseReplacement>> replacementsList = Lists.newArrayList();
        ProjectDto projectDto = projectService.getById(dataId);
        if (Objects.isNull(projectDto)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        List<Map<String, Object>> dataList = projectInfoService.findMaterialOfProject(projectDto.getProjectCode());
        if (CollectionUtils.isEmpty(dataList)) {
            return replacementsList;
        }
        List<Item> items = Lists.newArrayList();
        for (Map<String, Object> dataMap: dataList) {
            items.add(new Item(dataMap));
        }
        Map<String, List<Item>> itemMapBySupplier = items.stream().collect(Collectors.groupingBy(Item::getSupplierName));
        Set<Map.Entry<String, List<Item>>> entrySet = itemMapBySupplier.entrySet();
        Iterator<Map.Entry<String, List<Item>>> iterator = entrySet.iterator();
        String requirePart = projectDto.getRequirePart();
        String projectName = projectDto.getProjectName();
        String goodsReceiveAddress = projectDto.getGoodsReceiveAddress();
        String paymentType = projectDto.getPaymentType();
        Long goodsReceiveTime = projectDto.getGoodsReceiveTime();
        String goodsReceiveTimeStr = "";
        if (Objects.nonNull(goodsReceiveTime)) {
            Date date = Date.from(Instant.ofEpochMilli(goodsReceiveTime));
            goodsReceiveTimeStr = simpleDateFormat.format(date);
        }
        while (iterator.hasNext()) {
            List<BaseReplacement> replacements = Lists.newArrayList();
            Map.Entry<String, List<Item>> entry = iterator.next();
            String supplierName = entry.getKey();
            List<Item> itemList = entry.getValue();
            BigDecimal totalPriceWithTax = new BigDecimal("0");
            List<List<LinkedHashMap<String, String>>> tables = Lists.newArrayList();
            List<LinkedHashMap<String, String>> rowDataList = Lists.newArrayList();
            Set<String> materialNameSet = Sets.newHashSet();
            Item item;
            String materialName;
            for (int i = 0; i < itemList.size(); i ++) {
                item = itemList.get(i);
                materialName = item.getMaterialName();
                materialNameSet.add(materialName);
                LinkedHashMap<String, String> row = Maps.newLinkedHashMap();
                row.put("名称", item.getMaterialName());
                row.put("型号", item.getSpecification());
                row.put("品牌/产地", item.getBrand());
                row.put("单位", item.getUnit());
                String qty = item.getQuantity();
                if (StringUtils.isNotBlank(qty)) {
                    qty = new BigDecimal(qty).setScale(2, BigDecimal.ROUND_CEILING).toString();
                    row.put("数量", qty);
                } else {
                    row.put("数量", "");
                }
                row.put("单价（元）", item.getUnitPrice().setScale(2, BigDecimal.ROUND_CEILING).toString());
                row.put("金额（元）", item.getTotalPrice().setScale(2, BigDecimal.ROUND_CEILING).toString());
                rowDataList.add(row);
                totalPriceWithTax = totalPriceWithTax.add(item.getTotalPrice());
            }
            tables.add(rowDataList);
            String itemNames = Joiner.on("，").join(materialNameSet); // split with chinese syntax
            replacements.add(BaseReplacement.of("itemNames", itemNames, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("supplierName", supplierName, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("projectName", projectName, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("requirePart", requirePart, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("goodsReceiveAddress", goodsReceiveAddress, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("goodsReceiveTime", goodsReceiveTimeStr, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("paymentType", paymentType, ReplacementType.STRING));
            replacements.add(BaseReplacement.of("goodsList", tables, ReplacementType.TABLE, "黑体", 12));
            replacements.add(BaseReplacement.of("totalPriceWithTax", totalPriceWithTax.setScale(2, BigDecimal.ROUND_CEILING).toString(), ReplacementType.STRING));
            replacements.add(BaseReplacement.of("totalPriceWithTaxCN", RMBChange.change(totalPriceWithTax.setScale(2, BigDecimal.ROUND_CEILING).toString()), ReplacementType.STRING));
            replacementsList.add(replacements);
        }
        return replacementsList;
    }
}
