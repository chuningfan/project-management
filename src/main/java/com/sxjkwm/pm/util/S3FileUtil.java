package com.sxjkwm.pm.util;


import com.sxjkwm.pm.constants.Constant;
import io.minio.*;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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

    public String upload(Serializable projectIdentity, MultipartFile file, Constant.FileType fileType) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (InputStream inputStream = file.getInputStream()) {
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            String objectName = projectIdentity + "/" + fileName;
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(fileType.getValue()).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(file.getContentType()).build();
            minioClient.putObject(objectArgs);
            return objectName;
        }
    }

    public GetObjectResponse getFile(Constant.FileType fileType, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(fileType.getValue())
                .object(objectName).build();
        return minioClient.getObject(objectArgs);
    }

    public void remove(Constant.FileType fileType, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(fileType.getValue()).object(objectName).build());
    }

}
