package com.sxjkwm.pm.business.eplatform.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.eplatform.dao.EpDao;
import com.sxjkwm.pm.business.eplatform.dao.EsDao;
import com.sxjkwm.pm.common.PageDataDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Vic.Chu
 * @date 2022/8/8 15:31
 */
public abstract class EpBaseService<T> {

    private static final Logger logger = LoggerFactory.getLogger(EpBaseService.class);

    private final EpDao epDao;

    private final EsDao esDao;

    protected EpBaseService(EpDao epDao, EsDao esDao) {
        this.epDao = epDao;
        this.esDao = esDao;
    }

    protected List<T> queryFromDB() {
        List<Map<String, Object>> dataList = epDao.queryList(getSQL());
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        List<T> result = Lists.newArrayList();
        for (Map<String, Object> dataMap: dataList) {
            result.add(convert(dataMap));
        }
        return result;
    }

    protected void syncToEs(String index, String dataIdPropertyName) throws Exception {
        logger.info("Start to synchronize data from DB to ES ..., index = {}", index);
        esDao.deleteIndex(index);
        List<T> dtos = queryFromDB();
        if (CollectionUtils.isEmpty(dtos)) {
            return;
        }
        esDao.createIndex(index);
        String dataId;
        for (T dto: dtos) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dto));
            if (StringUtils.isNotBlank(dataIdPropertyName)) {
                Field field = dto.getClass().getDeclaredField(dataIdPropertyName);
                boolean originalAccess = field.isAccessible();
                field.setAccessible(true);
                dataId = field.get(dto).toString();
                field.setAccessible(originalAccess);
            } else {
                dataId = UUID.fastUUID().toString().replaceAll("-", "");
            }
            esDao.addData(jsonObject, index, dataId);
        }
        logger.info("Sync finished");
    }

    protected PageDataDto<Map<String, Object>> queryFromES(String index, AbstractQueryBuilder queryCondition, Integer pageSize, Integer pageNo, String sortField) throws IOException {
        return esDao.searchListData(index, queryCondition, pageSize, pageNo, null, sortField, null);
    }

    protected abstract T convert(Map<String, Object> dataRow);

    protected abstract String getSQL();

    protected static class ConditionPair {

        protected String key;

        protected String colName;

        protected Class<?> clazz;

        public ConditionPair(String key, String colName, Class<?> clazz) {
            this.key = key;
            this.colName = colName;
            this.clazz = clazz;
        }

        public String getKey() {
            return key;
        }

        public String getColName() {
            return colName;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public static ConditionPair of(String key, String colName, Class<?> clazz) {
            return new ConditionPair(key, colName, clazz);
        }

    }

    /**
     * This is just an indication for condition value
     */
    protected static class Status {
    }

}
