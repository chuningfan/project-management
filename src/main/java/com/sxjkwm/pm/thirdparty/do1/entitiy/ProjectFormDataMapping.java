package com.sxjkwm.pm.thirdparty.do1.entitiy;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/9/19 14:44
 *
 * 针对非配置化字段（项目进本信息）配置字段映射
 *
 */
@Entity
@Table(name = "pm_project_form_data_mapping")
public class ProjectFormDataMapping extends BaseEntity {

    @Column(name = "form_id")
    private String formId; // 道一云表单ID

    @Column(name = "flow_id")
    private Long flowId; // 流程ID

    @Column(name = "sys_field")
    private String sysField; // pm_project 表字段-对应的实体类name

    @Column(name = "field_id")
    private String fieldId; // 道一云表单字段ID

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
