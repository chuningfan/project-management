package com.sxjkwm.pm.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vic.Chu
 * @date 2022/6/25 9:52
 */
public class PinYinUtil {

    public static String convertChineseWordToPinYin(String chineseWords, String splitWith) {
        if (StringUtils.isBlank(chineseWords)) {
            return null;
        }
        char[] array = chineseWords.trim().toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder builder = new StringBuilder();
        for (char c: array) {
            if (Character.toString(c).matches("[\\u4e00-\\u9fa5]")) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    builder.append(temp[0]);
                    if (StringUtils.isNotBlank(splitWith)) {
                        builder.append(splitWith);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                }
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

}
