package com.sxjkwm.pm.common;

import com.sxjkwm.pm.constants.Constant;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "is_deleted", columnDefinition = "DEFAULT 0")
    protected Integer isDeleted = Constant.YesOrNo.NO.getValue();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

}
