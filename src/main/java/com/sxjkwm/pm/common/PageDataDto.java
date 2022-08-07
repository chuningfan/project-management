package com.sxjkwm.pm.common;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/8/7 13:26
 */
public class PageDataDto<T> implements Serializable {

    private List<T> content;

    private int totalPages;

    private int currentPageNo;

    private int currentPageSize;

    private long total;

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        this.currentPageNo = currentPageNo;
    }

    public int getCurrentPageSize() {
        return currentPageSize;
    }

    public void setCurrentPageSize(int currentPageSize) {
        this.currentPageSize = currentPageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
