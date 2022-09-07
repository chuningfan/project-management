package com.sxjkwm.pm.business.file.handler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.entity.User;
import com.sxjkwm.pm.business.centralizedpurchase.dto.Item;
import com.sxjkwm.pm.business.centralizedpurchase.service.ProjectInfoService;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.file.handler.replacement.ReplacementType;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.RMBChange;
import com.sxjkwm.pm.wxwork.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

/**
 * @author Vic.Chu
 * @date 2022/8/17 8:45
 */
@Component
public class SALEContractHandler implements PatternFileHandler {

    private final ProjectService projectService;

    private final UserService userService;

    private final ProjectInfoService projectInfoService;

    @Autowired
    public SALEContractHandler(ProjectService projectService, UserService userService, ProjectInfoService projectInfoService) {
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
        BigDecimal salePriceAdditionalRate = projectDto.getSalePriceAdditionalRate();
        if (Objects.isNull(salePriceAdditionalRate)) {
            salePriceAdditionalRate = new BigDecimal("5");
        }
        salePriceAdditionalRate = salePriceAdditionalRate.multiply(new BigDecimal("0.001")).add(new BigDecimal("1"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        replacements.add(BaseReplacement.of("projectName", projectDto.getProjectName(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("requirePart", projectDto.getRequirePart(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("purchaseScope", projectDto.getPurchaseScope(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("goodsReceiveAddress", projectDto.getGoodsReceiveAddress(), ReplacementType.STRING));
        Long goodsReceiveTime = projectDto.getGoodsReceiveTime();
        String goodsReceiveTimeStr = "";
        if (Objects.nonNull(goodsReceiveTime)) {
            Date date = Date.from(Instant.ofEpochMilli(goodsReceiveTime));
            goodsReceiveTimeStr = simpleDateFormat.format(date);
        }
        replacements.add(BaseReplacement.of("goodsReceiveTime", goodsReceiveTimeStr, ReplacementType.STRING));
        String ownerUserId = projectDto.getOwnerUserId();
        User owner = userService.findByUserId(ownerUserId);
        String ownerName = ContextHelper.getUserData().getUsername();
        if (Objects.isNull(owner)) {
            ownerName = owner.getName();
        }
        replacements.add(BaseReplacement.of("ownerName", ownerName, ReplacementType.STRING));
        replacements.add(BaseReplacement.of("ownerMobile", owner.getMobile(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("paymentType", projectDto.getPaymentType(), ReplacementType.STRING));
        List<Map<String, Object>> dataList = projectInfoService.findMaterialOfProject(projectDto.getProjectCode());
        if (CollectionUtils.isNotEmpty(dataList)) {
            List<List<LinkedHashMap<String, String>>> tables = Lists.newArrayList();
            List<LinkedHashMap<String, String>> rowDataList = Lists.newArrayList();
            BigDecimal totalPriceWithTax = new BigDecimal("0");
            List<Item> items = Lists.newArrayList();
            for (Map<String, Object> data: dataList) {
                Item item = new Item(data);
                items.add(item);
            }
            items.sort(Comparator.comparing(Item::getMaterialName));
            for (Item item: items) {
                BigDecimal unitPriceWithTax = item.getUnitPrice().multiply(salePriceAdditionalRate).setScale(2, BigDecimal.ROUND_CEILING);
                LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
                map.put("名称", item.getMaterialName());
                map.put("型号", item.getSpecification());
                map.put("品牌/产地", item.getBrand());
                map.put("单位", item.getUnit());
                String qty = item.getQuantity();
                if (StringUtils.isNotBlank(qty)) {
                    qty = new BigDecimal(qty).setScale(2, BigDecimal.ROUND_CEILING).toString();
                    map.put("数量", qty);
                } else {
                    map.put("数量", "");
                }
                map.put("单价（元）", unitPriceWithTax.toString());
                BigDecimal itemTotal = unitPriceWithTax.multiply(new BigDecimal(item.getQuantity())).setScale(2, BigDecimal.ROUND_CEILING);
                map.put("金额（元）", itemTotal.toString());
                totalPriceWithTax = totalPriceWithTax.add(itemTotal);
                rowDataList.add(map);
            }
            totalPriceWithTax = totalPriceWithTax.setScale(2, BigDecimal.ROUND_CEILING);
            tables.add(rowDataList);
            replacements.add(BaseReplacement.of("goodsList", tables, ReplacementType.TABLE, "黑体", 12));
            replacements.add(BaseReplacement.of("totalPriceWithTax", totalPriceWithTax.toString(), ReplacementType.STRING));
            replacements.add(BaseReplacement.of("totalPriceWithTaxCN", RMBChange.change(totalPriceWithTax.toString()), ReplacementType.STRING));
        }
        return replacements;
    }

    @Override
    public boolean isSingleFile() {
        return true;
    }
}
