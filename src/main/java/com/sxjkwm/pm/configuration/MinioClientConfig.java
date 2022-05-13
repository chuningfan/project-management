package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.constants.Constant;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class MinioClientConfig {

    @Bean
    public MinioClient minioClient(@Autowired OssConfig ossConfig) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(ossConfig.getOssAddress())
                .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey())
                .build();
        Constant.FileType[] fileTypes = Constant.FileType.values();
        for (Constant.FileType fileType: fileTypes) {
            String type = fileType.getValue();
            BucketExistsArgs args = BucketExistsArgs.builder().bucket(type).build();
            if (!minioClient.bucketExists(args)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(type).build());
            }
        }
        minioClient.setTimeout(60000, 60000, 60000);
        return minioClient;
    }
}
