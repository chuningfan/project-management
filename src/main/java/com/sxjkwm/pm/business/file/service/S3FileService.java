package com.sxjkwm.pm.business.file.service;

import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/5/12 21:06
 */
@Service
public class S3FileService {

    private final S3FileUtil s3FileUtil;

    private final ProjectFileDao projectFileDao;

    @Autowired
    public S3FileService(S3FileUtil s3FileUtil, ProjectFileDao projectFileDao) {
        this.s3FileUtil = s3FileUtil;
        this.projectFileDao = projectFileDao;
    }

    public void downloadByFileId(Long fileId, HttpServletResponse response) throws PmException{
        ProjectFile projectFile = projectFileDao.getOne(fileId);
        Long projectId = projectFile.getProjectId();
        String fileType = projectFile.getFileType();
        Long nodeId = projectFile.getProjectNodeId();
        this.download(projectId, nodeId, fileType, response);
    }

    public Boolean removeFileById(Long fileId) throws PmException {
        try {
            ProjectFile projectFile = projectFileDao.getOne(fileId);
            String fileType = projectFile.getFileType();
            String objectName = projectFile.getObjName();
            projectFileDao.delete(projectFile);
            s3FileUtil.remove(Constant.FileType.valueOf(fileType.toUpperCase(Locale.ROOT)), objectName);
            return true;
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException | NoSuchAlgorithmException | InvalidKeyException |
                InvalidResponseException | XmlParserException | InternalException e) {
            throw getException(e);
        }
    }

    @Transactional
    public Long upload(Long projectId, MultipartFile file, Constant.FileType fileType, Long projectNodeId, String fileName) throws PmException {
        PmError error = null;
        try {
            ProjectFile condition = new ProjectFile();
            condition.setProjectNodeId(projectNodeId);
            condition.setProjectId(projectId);
            condition.setFileType(fileType.getValue());
            ProjectFile existingFile = projectFileDao.findOne(Example.of(condition)).orElse(null);
            if (Objects.nonNull(existingFile)) {
                projectFileDao.delete(existingFile);
                s3FileUtil.remove(fileType, existingFile.getObjName());
            }
            String objectName = s3FileUtil.upload(projectId, file, fileType);
            ProjectFile projectFile = new ProjectFile();
            projectFile.setObjName(objectName);
            projectFile.setFileName(fileName);
            projectFile.setProjectId(projectId);
            projectFile.setProjectNodeId(projectNodeId);
            projectFile.setFileType(fileType.getValue());
            projectFile = projectFileDao.save(projectFile);
            return projectFile.getId();
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            throw getException(e);
        }
    }

    public void download(Long projectId, Long nodeId, String fileType, HttpServletResponse response) throws PmException {
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFlowNodeId(nodeId);
        projectFile.setProjectId(projectId);
        Constant.FileType type = Constant.FileType.valueOf(fileType.toUpperCase(Locale.ROOT));
        projectFile.setFileType(type.getValue());
        projectFile = projectFileDao.findOne(Example.of(projectFile)).orElse(null);
        if (Objects.nonNull(projectFile)) {
            String objectName = projectFile.getObjName();
            String fileName = projectFile.getFileName();
            String fType = new MimetypesFileTypeMap().getContentType(fileName);
            response.reset();
            response.setHeader("content-type", fType + ";charset=utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try (GetObjectResponse getObjectResponse = s3FileUtil.getFile(type, objectName)) {
                byte[] buf = new byte[1024];
                int len;
                try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                    while ((len=getObjectResponse.read(buf))!=-1){
                        os.write(buf,0,len);
                    }
                    os.flush();
                    byte[] bytes = os.toByteArray();
                    response.setCharacterEncoding("utf-8");
                    response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                    try (ServletOutputStream stream = response.getOutputStream()){
                        stream.write(bytes);
                        stream.flush();
                    }
                }
            } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                    NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
                throw getException(e);
            }
        } else {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
    }

    private PmException getException(Exception e) {
        PmException pmException = new PmException(PmError.S3_SERVICE_ERROR);
        pmException.appendMsg(e.getMessage());
        // TODO
        return pmException;
    }

}
