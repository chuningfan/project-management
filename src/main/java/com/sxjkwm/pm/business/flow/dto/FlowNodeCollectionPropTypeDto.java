package com.sxjkwm.pm.business.flow.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/9/25 11:20
 */
public class FlowNodeCollectionPropTypeDto implements Serializable {

    private String label;

    private String type;

    private String dbType;

    public FlowNodeCollectionPropTypeDto(String label, String type, String dbType) {
        this.label = label;
        this.type = type;
        this.dbType = dbType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
