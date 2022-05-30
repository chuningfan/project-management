package com.sxjkwm.pm.business.file.service;

import com.google.common.collect.Sets;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.FileUtil;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Set;

/**
 * @author Vic.Chu
 * @date 2022/5/28 7:06
 */
@Service
public class PreviewService {

    private static final Set<String> supportedSuffixes = Sets.newHashSet("txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx");

    private final ProjectFileDao projectFileDao;

    private final S3FileUtil s3FileUtil;

    @Autowired
    public PreviewService(ProjectFileDao projectFileDao, S3FileUtil s3FileUtil) {
        this.projectFileDao = projectFileDao;
        this.s3FileUtil = s3FileUtil;
    }

    public void preview(Long fileId, HttpServletResponse response) throws PmException {
        ProjectFile projectFile = projectFileDao.getOne(fileId);
        if (Objects.isNull(projectFile)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        String fileName = projectFile.getFileName();
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if ("xlsx".equals(fileSuffix)) {
            fileSuffix = "xls";
        }
        if ("docx".equals(fileSuffix)) {
            fileSuffix = "doc";
        }
        if(!supportedSuffixes.contains(fileSuffix)){
            throw new PmException(PmError.FILE_PREVIEW_NOT_SUPPORTED);
        }
        try (GetObjectResponse objectResponse = s3FileUtil.getFile(projectFile.getBucketName(), projectFile.getObjectName());
             InputStream convertedStream = FileUtil.covertCommonByStream(objectResponse, fileSuffix);
             OutputStream outputStream = response.getOutputStream();) {
            byte[] buff =new byte[1024];
            int n;
            while((n=convertedStream.read(buff))!=-1){
                outputStream.write(buff,0,n);
            }
            outputStream.flush();
        } catch (Exception e) {
            throw new PmException(PmError.INTERNAL_ERROR);
        }
    }

}
