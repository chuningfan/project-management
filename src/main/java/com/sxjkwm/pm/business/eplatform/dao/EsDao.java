package com.sxjkwm.pm.business.eplatform.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.common.PageDataDto;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Vic.Chu
 * @date 2022/8/6 11:19
 */
@Component
public class EsDao {

    private static final Logger logger = LoggerFactory.getLogger(EsDao.class);

    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public EsDao(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public boolean isIndexExist(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    public boolean createIndex(String index) throws IOException {
        if(isIndexExist(index)){
            logger.error("Index is exits!");
            return false;
        }
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        //2.执行客户端请求
        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        logger.info("Create index successfully",index);

        return response.isAcknowledged();
    }

    public boolean deleteIndex(String index) throws IOException {
        if(!isIndexExist(index)) {
            logger.error("Index is not exits!");
            return false;
        }
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        logger.info("删除索引{}成功",index);
        return delete.isAcknowledged();
    }

    public String addData(JSONObject jsonObject, String index, String id) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.id(id);
        request.timeout(TimeValue.timeValueSeconds(1));
        request.source(jsonObject, XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        logger.info("Add data successfully, index: {}, status: {}, id: {}",index,response.status().getStatus(), response.getId());
        return response.getId();
    }

    public String addData(JSONObject jsonObject, String index) throws IOException {
        return addData(jsonObject, index, UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
    }

    public void deleteDataById(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, id);
        DeleteResponse delete = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        logger.info("index:{}, id:{}", index, id);
    }

    public Boolean updateDataById(Object object, String index, String id) throws IOException {
        //更新请求 整体更新 直接开大
        UpdateRequest update = new UpdateRequest(index, id);
        update.timeout("1s");
        update.doc(JSON.toJSONString(object), XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(update, RequestOptions.DEFAULT);
        logger.info("index:{}, id:{}, Updated",index, id);
        if (updateResponse.status().getStatus() == RestStatus.OK.getStatus()) {
            return Boolean.TRUE;
        }
        logger.error("Update data failed, errorCode: {}", updateResponse.status().getStatus());
        return Boolean.FALSE;
    }

    public Boolean updateDataByIdNoRealTime(Object object, String index, String id) throws IOException {
        //更新请求
        UpdateRequest update = new UpdateRequest(index, id);
        //保证数据实时更新
        update.setRefreshPolicy("wait_for");
        update.timeout("1s");
        update.doc(JSON.toJSONString(object), XContentType.JSON);
        //执行更新请求
        UpdateResponse updateResponse = restHighLevelClient.update(update, RequestOptions.DEFAULT);
        logger.info("index:{}, id:{}, Updated",index, id);
        if (updateResponse.status().getStatus() == RestStatus.OK.getStatus()) {
            return Boolean.TRUE;
        }
        logger.error("Update data failed, errorCode: {}", updateResponse.status().getStatus());
        return Boolean.FALSE;
    }

    public Map<String,Object> searchDataById(String index, String id, String fields) throws IOException {
        GetRequest request = new GetRequest(index, id);
        if (StringUtils.isNotEmpty(fields)){
            request.fetchSourceContext(new FetchSourceContext(true,fields.split(","), Strings.EMPTY_ARRAY));
        }
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        Map<String, Object> map = response.getSource();
        if (Objects.isNull(map)) {
            return null;
        }
        map.put("id",response.getId());
        return map;
    }

    public  boolean existsById(String index,String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }

    public List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField) {
        //解析结果
        List<Map<String,Object>> list = Lists.newArrayList();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> high = hit.getHighlightFields();
            HighlightField title = high.get(highlightField);
            hit.getSourceAsMap().put("id", hit.getId());
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
            //解析高亮字段,将原来的字段换为高亮字段
            if (title!=null){
                Text[] texts = title.fragments();
                String nTitle="";
                for (Text text : texts) {
                    nTitle+=text;
                }
                //替换
                sourceAsMap.put(highlightField,nTitle);
            }
            list.add(sourceAsMap);
        }
        return list;
    }

    public PageDataDto<Map<String, Object>> searchListData(String index,
                                                    AbstractQueryBuilder queryBuilder,
                                                    Integer pageSize,
                                                    Integer pageNo,
                                                    String fields,
                                                    String sortField,
                                                    String highlightField) throws IOException {

        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (StringUtils.isNotEmpty(fields)){
            searchSourceBuilder.fetchSource(new FetchSourceContext(true,fields.split(","),Strings.EMPTY_ARRAY));
        }
        Integer from = (pageNo - 1) <= 0 ? 0 : (pageNo - 1) * pageSize;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(pageSize);
        if (StringUtils.isNotEmpty(sortField)){
            searchSourceBuilder.sort(sortField+".keyword", SortOrder.ASC);
        }
        if (Objects.nonNull(queryBuilder)) {
            searchSourceBuilder.query(queryBuilder);
        }
        if (Objects.nonNull(highlightField)) {
            HighlightBuilder highlight = new HighlightBuilder();
            highlight.field(highlightField);
            highlight.requireFieldMatch(false);
            highlight.preTags("<span style='color:red'>");
            highlight.postTags("</span>");
            searchSourceBuilder.highlighter(highlight);
        }
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        long totalHits = response.getHits().getTotalHits().value;
        logger.warn("=="+response.getHits().getTotalHits());
        if (response.status().getStatus() == 200) {
            List<Map<String, Object>> dataList = setSearchResponse(response, highlightField);
            PageDataDto<Map<String, Object>> pageDataDto = new PageDataDto<>();
            pageDataDto.setContent(dataList);
            Long pageNum = totalHits/pageSize + (totalHits % pageSize == 0 ? 0 : 1);
            pageDataDto.setTotalPages(pageNum.intValue());
            pageDataDto.setCurrentPageNo(pageNo);
            pageDataDto.setCurrentPageSize(pageSize);
            pageDataDto.setTotal(totalHits);
            return pageDataDto;
        }
        return null;
    }

}
