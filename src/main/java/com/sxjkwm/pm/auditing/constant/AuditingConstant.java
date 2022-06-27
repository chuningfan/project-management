package com.sxjkwm.pm.auditing.constant;

import com.sxjkwm.pm.auditing.handler.AuditingDataHandler;
import com.sxjkwm.pm.auditing.handler.impl.ProjectAuditingDataHandler;
import com.sxjkwm.pm.util.ContextUtil;

/**
 * @author Vic.Chu
 * @date 2022/6/7 9:25
 */
public interface AuditingConstant {

    enum Action {
        APPROVE(1, "通过"), REJECT(0, "驳回"),
        ;
        Action(Integer action, String label) {
        this.action = action;
        this.label = label;
    }

        private Integer action;

        private String label;

        public Integer getAction() {
            return action;
        }

        public void setAction(Integer action) {
            this.action = action;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    enum AuditingType {
        OR(0, "或签"), AND(1, "会签"),
        ;

        AuditingType(Integer type, String label) {
            this.type = type;
            this.label = label;
        }

        private Integer type;

        private String label;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    enum AuditingDataType {

        PROJECT(1, "项目") {
            @Override
            public AuditingDataHandler dataHandler() {
                return ContextUtil.getBean(ProjectAuditingDataHandler.class);
            }
        },
        ;

        private Integer dataType;

        private String label;

        AuditingDataType(Integer dataType, String label) {
            this.dataType = dataType;
            this.label = label;
        }

        public Integer getDataType() {
            return dataType;
        }

        public void setDataType(Integer dataType) {
            this.dataType = dataType;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public abstract AuditingDataHandler dataHandler();

        public static AuditingDataType getFromType(Integer dataType) {
            switch(dataType) {
                default: return PROJECT;
            }
        }
    }

    enum AuditingStatus {
        SAVED(0, "flow saved"), IN_PROGRESS(1, "in progress"), COMPLETED(2, "flow is over"), REJECTED(3, "rejected"),
        ABORTED(4, "aborted"),
        ;

        private Integer status;

        private String label;

        AuditingStatus(Integer status, String label) {
            this.status = status;
            this.label = label;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

}
