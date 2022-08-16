package com.sxjkwm.pm.business.eplatform.task.entity;

import com.sxjkwm.pm.common.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Vic.Chu
 * @date 2022/8/15 9:10
 */
@Entity
@Table(name = "ep_task_config", indexes = {@Index(name = "eptask_thirdpname", columnList = "third_party_name")})
public class TaskConfig extends BaseEntity {

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "net_price_url")
    private String netPriceUrl;

    @Column(name = "method_type")
    private String methodType;

    @Column(name = "url_params", columnDefinition = "TEXT")
    private String params;  // JSON string

    @Column(name = "parser_bean_name")
    private String parserBeanName;

    @Column(name = "data_sync_bean_name")
    private String dataSynchronizerBeanName;

    @Column(name = "third_party_name")
    private String thirdPartyName;

    @Column(name = "price_termination_way")
    private Integer priceTerminationWay;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getNetPriceUrl() {
        return netPriceUrl;
    }

    public void setNetPriceUrl(String netPriceUrl) {
        this.netPriceUrl = netPriceUrl;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParserBeanName() {
        return parserBeanName;
    }

    public void setParserBeanName(String parserBeanName) {
        this.parserBeanName = parserBeanName;
    }

    public String getDataSynchronizerBeanName() {
        return dataSynchronizerBeanName;
    }

    public void setDataSynchronizerBeanName(String dataSynchronizerBeanName) {
        this.dataSynchronizerBeanName = dataSynchronizerBeanName;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public Integer getPriceTerminationWay() {
        return priceTerminationWay;
    }

    public void setPriceTerminationWay(Integer priceTerminationWay) {
        this.priceTerminationWay = priceTerminationWay;
    }
}
