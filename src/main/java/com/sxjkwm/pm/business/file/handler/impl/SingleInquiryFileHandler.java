package com.sxjkwm.pm.business.file.handler.impl;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.file.handler.replacement.ReplacementType;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/7/8 17:44
 */
@Component
public class SingleInquiryFileHandler implements PatternFileHandler {

    private final ProjectService projectService;

    @Autowired
    public SingleInquiryFileHandler(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) throws PmException {
        List<BaseReplacement> replacements = Lists.newArrayList();
        ProjectDto projectDto = projectService.getById(dataId);
        if (Objects.isNull(projectDto)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        Long projectTime = projectDto.getProjectTime();
        String projectTimeStr = "";
        if (Objects.nonNull(projectTime)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = Date.from(Instant.ofEpochMilli(projectTime));
            projectTimeStr = simpleDateFormat.format(date);
        }
        replacements.add(BaseReplacement.of("projectName", projectDto.getProjectName(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("projectCode", projectDto.getProjectCode(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("projectTime", projectTimeStr, ReplacementType.STRING));
        replacements.add(BaseReplacement.of("projectDescription", projectDto.getDescription(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("requirePart", projectDto.getRequirePart(), ReplacementType.STRING));
        replacements.add(BaseReplacement.of("purchaseScope", projectDto.getPurchaseScope(), ReplacementType.STRING));



//        List<List<TreeMap<String, String>>> tables = Lists.newArrayList();
//        // table1
//        List<TreeMap<String, String>> table1 = Lists.newArrayList();
//        TreeMap<String, String> row1_1 = Maps.newTreeMap();
//        table1.add(row1_1);
//        row1_1.put("表1第一列", "第一行第一列数据");
//        row1_1.put("表1第二列", "第一行第二列数据");
//        TreeMap<String, String> row2_1 = Maps.newTreeMap();
//        table1.add(row2_1);
//        row2_1.put("表1第一列", "第二行第一列数据");
//        row2_1.put("表1第二列", "第二行第二列数据");
//        tables.add(table1);
//        // table2
//        List<TreeMap<String, String>> table2 = Lists.newArrayList();
//        TreeMap<String, String> row1_2 = Maps.newTreeMap();
//        table2.add(row1_2);
//        row1_2.put("表2第一列", "第一行第一列数据");
//        row1_2.put("表2第二列", "第一行第二列数据");
//        row1_2.put("表2第三列", "第一行第三列数据");
//        TreeMap<String, String> row2_2 = Maps.newTreeMap();
//        table2.add(row2_2);
//        row2_2.put("表2第一列", "第二行第一列数据");
//        row2_2.put("表2第二列", "第二行第二列数据");
//        row2_2.put("表2第三列", "第二行第三列数据");
//        tables.add(table2);
//        replacements.add(BaseReplacement.of("goodsList", tables, ReplacementType.TABLE));
        // ----------------------------------------------------------
//        TreeMap<String, List<TreeMap<String, String>>> tables = Maps.newTreeMap();
//        List<TreeMap<String, String>> rowDataList = Lists.newArrayList();
//        TreeMap<String, String> row1_1 = Maps.newTreeMap();
//        row1_1.put("表1第一列", "第一行第一列数据");
//        row1_1.put("表1第二列", "第一行第二列数据");
//        row1_1.put("表1第三列", "第一行第三列数据");
//        rowDataList.add(row1_1);
//        TreeMap<String, String> row1_2 = Maps.newTreeMap();
//        row1_2.put("表1第一列", "第二行第一列数据");
//        row1_2.put("表1第二列", "第二行第二列数据");
//        row1_2.put("表1第三列", "第二行第三列数据");
//        rowDataList.add(row1_2);
//        tables.put("标包1", rowDataList);
//        replacements.add(BaseReplacement.of("goodsList", tables, ReplacementType.TABLE_WITH_MERGED_FIRST_COLUMN, "黑体", 12));
//        replacements.add(BaseReplacement.of("supplyPeriod", "2022年2月1日", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("goodsReceiveAddress", "xxx收费站", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("ownerName", "张三", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("deadline", "2022年1月15日 10点00分00秒", ReplacementType.STRING, "黑体", 12));
        return replacements;
    }

}
