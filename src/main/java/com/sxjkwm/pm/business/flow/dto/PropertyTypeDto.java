package com.sxjkwm.pm.business.flow.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/5/26 15:56
 */
public class PropertyTypeDto implements Serializable {

    private String value;

    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
