package com.sxjkwm.pm.business.file.handler.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.file.handler.replacement.ReplacementType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FontFamily;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

/**
 * @author Vic.Chu
 * @date 2022/7/8 17:44
 */
@Component
public class SingleInquiryFileHandler implements PatternFileHandler {

    @Override
    public List<BaseReplacement> captureData(Long dataId, Long flowId, Long flowNodeId, Long propertyDefId) {
        List<BaseReplacement> replacements = Lists.newArrayList();
//        replacements.add(BaseReplacement.of("projectName", "xxx测试项目", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("projectCode", "CGRW-0010203023", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("projectTime", "2022年1月1日", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("projectDescription", "巴拉巴拉阿巴阿巴", ReplacementType.STRING, "黑体", 12));
//        replacements.add(BaseReplacement.of("requirePart", "高速电子", ReplacementType.STRING));
//        replacements.add(BaseReplacement.of("purchaseScope", "要购买黄瓜、茄子、豆角", ReplacementType.STRING));
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
