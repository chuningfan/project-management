package com.sxjkwm.pm.util;


import io.minio.*;
import io.minio.errors.*;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Vic.Chu
 *
 * This is for operations of files on S3-service
 */
@Component
public class S3FileUtil {

    private final MinioClient minioClient;

    @Autowired
    public S3FileUtil(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String upload(String bucketName, String objectName, MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            minioClient.putObject(objectArgs);
            return objectName;
        }
    }

    public String upload(String bucketName, String objectName, String fileName, InputStream fileInputStream) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (InputStream inputStream = fileInputStream) {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(bucketName).object(objectName)
                    .stream(inputStream, fileInputStream.available(), -1).contentType(MediaTypeFactory.getMediaType(fileName).get().toString()).build();
            minioClient.putObject(objectArgs);
            return objectName;
        }
    }

    public GetObjectResponse getFile(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName)
                .object(objectName).build();
        return minioClient.getObject(objectArgs);
    }

    public void remove(String bucketName, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

//    public GetObjectResponse getFile(String bucketName) {
//        GetObjectArgs.builder().bucket(bucketName).
//
//    }

}
