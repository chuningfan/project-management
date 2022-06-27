package com.sxjkwm.pm.auditing.dto;

import com.sxjkwm.pm.business.flow.dto.FlowNodeCollectionDefDto;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/6/8 9:58
 */
public class AuditingDataDto implements Serializable {

    private String propertyName;

    private Integer propertyIndex;

    private String propertyType;

    private String propertyValue;

    private List<FlowNodeCollectionDefDto> flowNodeCollectionDefDtos;

    private List<Map<String, Object>> dataList;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getPropertyIndex() {
        return propertyIndex;
    }

    public void setPropertyIndex(Integer propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public List<FlowNodeCollectionDefDto> getFlowNodeCollectionDefDtos() {
        return flowNodeCollectionDefDtos;
    }

    public void setFlowNodeCollectionDefDtos(List<FlowNodeCollectionDefDto> flowNodeCollectionDefDtos) {
        this.flowNodeCollectionDefDtos = flowNodeCollectionDefDtos;
    }

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

}
