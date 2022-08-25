package com.sxjkwm.pm.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/8/24 8:51
 */
public class PageRequest implements Serializable {

    private Integer pageSize;

    private Integer pageNo;

    private Long startTime;

    private Long endTime;

    public Integer getPageSize() {
        if (Objects.isNull(pageSize)) {
            pageSize = 20;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        if (Objects.isNull(pageNo)) {
            pageNo = 1;
        }
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
