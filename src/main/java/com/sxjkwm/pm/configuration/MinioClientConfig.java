package com.sxjkwm.pm.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.flow.dao.FlowDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.entity.Flow;
import com.sxjkwm.pm.business.flow.entity.FlowNode;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class MinioClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(MinioClientConfig.class);

    public static final Map<Long, String> FLOW_VALUE_DIC = Maps.newConcurrentMap();
    public static final Map<Long, String> FLOW_NODE_VALUE_DIC = Maps.newConcurrentMap();


    @Bean
    public MinioClient minioClient(@Autowired OssConfig ossConfig, @Autowired FlowDao flowDao, @Autowired FlowNodeDao flowNodeDao, @Autowired FlowNodeDefinitionDao flowNodeDefinitionDao) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ossConfig.getOssAddress())
                .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey())
                .build();
        logger.info("Initializing file buckets");
        initBuckets(minioClient, flowDao, flowNodeDao, flowNodeDefinitionDao);
        minioClient.setTimeout(60000, 60000, 60000);
        return minioClient;
    }

    public static void initBuckets(MinioClient minioClient, FlowDao flowDao, FlowNodeDao flowNodeDao, FlowNodeDefinitionDao flowNodeDefinitionDao) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        FLOW_VALUE_DIC.clear();
        FLOW_NODE_VALUE_DIC.clear();
        List<Flow> flows =  flowDao.findAll();
        if (CollectionUtils.isNotEmpty(flows)) {
            flows = flows.stream().filter(f -> f.getIsDeleted().intValue() == 0).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(flows)) {
                for (Flow flow: flows) {
                    String flowValue = flow.getFlowValue();
                    Long flowId = flow.getId();
                    List<FlowNode> flowNodes = flowNodeDao.getAvailableNodes(flowId);
                    if (CollectionUtils.isEmpty(flowNodes)) {
                        continue;
                    }
                    FLOW_VALUE_DIC.put(flowId, flowValue);
                    for (FlowNode flowNode: flowNodes) {
                        Long flowNodeId = flowNode.getId();
                        List<FlowNodeDefinition> definitions = flowNodeDefinitionDao.getByFlowNodeId(flowNodeId);
                        if (CollectionUtils.isEmpty(definitions)) {
                            continue;
                        }
                        String flowNodeValue = flowNode.getFlowNodeValue();
                        definitions = definitions.stream().filter(d -> d.getPropertyType().equalsIgnoreCase("FILE")).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(definitions)) {
                            continue;
                        }
                        FLOW_NODE_VALUE_DIC.put(flowNodeId, flowNodeValue);
                        for (FlowNodeDefinition fileDef: definitions) {
                            String bucketName = flowValue + flowNodeValue + fileDef.getPropertyKey().toLowerCase();
                            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
                            if (!minioClient.bucketExists(args)) {
                                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                            }
                        }
                    }
                }
            }
        }
    }
}
