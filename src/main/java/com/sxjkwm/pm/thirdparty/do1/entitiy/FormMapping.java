package com.sxjkwm.pm.thirdparty.do1.entitiy;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/9/17 13:36
 */
@Entity
@Table(name = "do1_form_mapping")
public class FormMapping extends BaseEntity {

    @Column(name = "form_id")
    private String formId;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "form_type")
    private String formType;   // 前端写死，例如：finance(财务）、business(业务)、humanResource(人事) 等

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }
}
