package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.business.flow.dao.FlowDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDao;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(MinioClientConfig.class);

    private static final String[] bucketNames = {"oddbucket", "evenbucket", "pattern", "finance", "temp"};

    @Bean
    public MinioClient minioClient(@Autowired OssConfig ossConfig) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ossConfig.getOssAddress())
                .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey())
                .build();
        logger.info("Initializing file buckets");
        minioClient.setTimeout(Long.valueOf(ossConfig.getConnectionTimeout()), Long.valueOf(ossConfig.getWriteTimeout()), Long.valueOf(ossConfig.getReadTimeout()));
        initBuckets(minioClient);
        return minioClient;
    }

    public static void initBuckets(MinioClient minioClient) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        for (String bucketName: bucketNames) {
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
            if (!minioClient.bucketExists(args)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        }
    }
}
