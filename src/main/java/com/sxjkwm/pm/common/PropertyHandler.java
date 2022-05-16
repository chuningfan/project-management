package com.sxjkwm.pm.common;

import java.util.List;

/**
 * @author Vic.Chu
 * @date 2022/5/15 14:18
 */
public interface PropertyHandler<T extends BaseCollectionProperty> {

    List<T> save(List<T> dataList);

    List<T> update(List<T> dataList);

    List<T> query(Long projectId, Long projectNodeId, String propertyKey);

    boolean delete(List<T> dataList);

}
