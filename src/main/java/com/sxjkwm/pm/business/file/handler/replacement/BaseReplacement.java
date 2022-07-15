package com.sxjkwm.pm.business.file.handler.replacement;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/7/8 14:21
 */
public class BaseReplacement implements Serializable {

    protected String key;

    protected Object data;

    protected ReplacementType replacementType;

    protected String fontFamily;

    protected Integer fontSize;

    public static BaseReplacement of(String key, Object data, ReplacementType replacementType) {
        return new BaseReplacement(key, data, replacementType, null, null);
    }

    public static BaseReplacement of(String key, Object data, ReplacementType replacementType, String fontFamily, Integer fontSize) {
        return new BaseReplacement(key, data, replacementType, fontFamily, fontSize);
    }

    public BaseReplacement(String key, Object data, ReplacementType replacementType, String fontFamily, Integer fontSize) {
        this.key = key;
        this.data = data;
        this.replacementType = replacementType;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }

    public String getKey() {
        return key;
    }

    public Object getData() {
        return data;
    }

    public ReplacementType getReplacementType() {
        return replacementType;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public Integer getFontSize() {
        return fontSize;
    }
}
