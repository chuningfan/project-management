package com.sxjkwm.pm.thirdparty.do1.dto;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/9/19 16:58
 */
public class ProjectFormDataMappingDto implements Serializable {

    private Long id;

    private String formId; // 道一云表单ID

    private Long flowId; // 流程ID

    private String sysField; // pm_project 表字段-对应的实体类name

    private String fieldId; // 道一云表单字段ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Long getFlowId() {
        return flowId;
    }

    public void setFlowId(Long flowId) {
        this.flowId = flowId;
    }

    public String getSysField() {
        return sysField;
    }

    public void setSysField(String sysField) {
        this.sysField = sysField;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

}
