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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

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
        List<T> dtos = queryFromDB();
        if (CollectionUtils.isEmpty(dtos)) {
            return;
        }
        String dataId;
        if (StringUtils.isNotBlank(dataIdPropertyName)) {
            if (esDao.isIndexExist(index)) {
                DecimalFormat df = new DecimalFormat("0.00");
                df.setRoundingMode(RoundingMode.DOWN);
                logger.warn("Index {} is existing", index);
                List<Map<String, Object>> existingIdMaps = esDao.searchAllUnderIndex(index, dataIdPropertyName);
                List<String> existingDataIds = Lists.newArrayList();
                for (Map<String, Object> idMap: existingIdMaps) {
                    existingDataIds.add(idMap.get(dataIdPropertyName).toString());
                }
                List<String> dataIds = Lists.newArrayList();
                for (T dto : dtos) {
                    Field field = dto.getClass().getDeclaredField(dataIdPropertyName);
                    boolean originalAccess = field.isAccessible();
                    field.setAccessible(true);
                    dataId = field.get(dto).toString();
                    field.setAccessible(originalAccess);
                    Map<String,Object> existingData = esDao.searchDataById(index, dataId, null);
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dto));
                    jsonObject.put("id", dataId);
                    logger.warn("Input data is {}, data id is {}", jsonObject, dataId);
                    if (Objects.isNull(existingData) || existingData.isEmpty()) { // no data exists, create new record directly
                        logger.warn("dataId = {} is not existing, create new record directly", dataId);
                        esDao.addData(jsonObject, index, dataId);
                    } else { // otherwise, compare existing data and input data
                        Set<Map.Entry<String, Object>> entrySet = existingData.entrySet();
                        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, Object> entry = iterator.next();
                            Object obj = entry.getValue();
                            if ((obj instanceof Double) && Objects.nonNull(obj)) {
                                String fmtVal = df.format(obj);
                                obj = new BigDecimal(fmtVal);
                                entry.setValue(obj);
                            }
                        }
                        JSONObject existingJSON = JSONObject.parseObject(JSONObject.toJSONString(existingData));
                        if (!jsonObject.equals(existingJSON)) { // If input data is different with existing data, replace
                            esDao.updateDataById(jsonObject, index, dataId);
                        }
                    }
                    dataIds.add(dataId);
                }
                // delete redundant data
                for (String id: existingDataIds) {
                    if (!dataIds.contains(id)) {
                        esDao.deleteDataById(index, id);
                    }
                }
            } else {
                esDao.createIndex(index);
                for (T dto : dtos) {
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dto));
                    Field field = dto.getClass().getDeclaredField(dataIdPropertyName);
                    boolean originalAccess = field.isAccessible();
                    field.setAccessible(true);
                    dataId = field.get(dto).toString();
                    field.setAccessible(originalAccess);
                    esDao.addData(jsonObject, index, dataId);
                }
            }
        } else {
            esDao.deleteIndex(index);
            esDao.createIndex(index);
            for (T dto : dtos) {
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(dto));
                dataId = UUID.fastUUID().toString().replaceAll("-", "");
                esDao.addData(jsonObject, index, dataId);
            }
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
