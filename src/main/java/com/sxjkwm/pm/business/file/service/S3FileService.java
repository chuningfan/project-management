package com.sxjkwm.pm.business.file.service;

import cn.hutool.core.lang.UUID;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.flow.dao.FlowNodeDefinitionDao;
import com.sxjkwm.pm.business.flow.entity.FlowNodeDefinition;
import com.sxjkwm.pm.business.project.dao.ProjectDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodeDao;
import com.sxjkwm.pm.business.project.dao.ProjectNodePropertyDao;
import com.sxjkwm.pm.business.project.entity.Project;
import com.sxjkwm.pm.business.project.entity.ProjectNode;
import com.sxjkwm.pm.business.project.entity.ProjectNodeProperty;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.OSSUtil;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
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
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/5/12 21:06
 */
@Service
public class S3FileService {

    private final S3FileUtil s3FileUtil;

    private final ProjectFileDao projectFileDao;

    private final ProjectDao projectDao;

    private final ProjectNodeDao projectNodeDao;

    private final ProjectNodePropertyDao projectNodePropertyDao;

    private final FlowNodeDefinitionDao flowNodeDefinitionDao;

    @Autowired
    public S3FileService(S3FileUtil s3FileUtil, ProjectFileDao projectFileDao, ProjectDao projectDao, ProjectNodeDao projectNodeDao, ProjectNodePropertyDao projectNodePropertyDao, FlowNodeDefinitionDao flowNodeDefinitionDao) {
        this.s3FileUtil = s3FileUtil;
        this.projectFileDao = projectFileDao;
        this.projectDao = projectDao;
        this.projectNodeDao = projectNodeDao;
        this.projectNodePropertyDao = projectNodePropertyDao;
        this.flowNodeDefinitionDao = flowNodeDefinitionDao;
    }

    public Boolean removeFileById(Long fileId) throws PmException {
        try {
            ProjectFile projectFile = projectFileDao.getOne(fileId);
            String bucketName = projectFile.getBucketName();
            String objectName = projectFile.getObjectName();
            projectFileDao.delete(projectFile);
            s3FileUtil.remove(bucketName, objectName);
            return true;
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException | NoSuchAlgorithmException | InvalidKeyException |
                InvalidResponseException | XmlParserException | InternalException e) {
            throw getException(e);
        }
    }

    @Transactional
    public Long upload(Long projectId, MultipartFile file, Long flowNodeId, Long propertyDefId) throws PmException {
        String bucketName = OSSUtil.getBucketName(projectId);
        String objectName = projectId + "/" + flowNodeId + "/" + (UUID.fastUUID().toString().replace("-", "").toLowerCase());
        try {
            if (file.isEmpty()) {
                throw new PmException(PmError.NO_FILE_PROVIDED);
            }
            String fileName = file.getOriginalFilename();
            Project project = projectDao.getOne(projectId);
            ProjectFile condition = new ProjectFile();
            condition.setFlowNodeId(flowNodeId);
            condition.setProjectId(projectId);
            condition.setPropertyDefId(propertyDefId);
            ProjectFile existingFile = projectFileDao.findOne(Example.of(condition)).orElse(null);
            if (Objects.nonNull(existingFile)) {
                removeFileById(existingFile.getId());
            }
            s3FileUtil.upload(bucketName, file, objectName);
            ProjectFile projectFile = new ProjectFile();
            projectFile.setObjectName(objectName);
            projectFile.setFileName(fileName);
            projectFile.setProjectId(projectId);
            projectFile.setFlowNodeId(flowNodeId);
            projectFile.setBucketName(bucketName);
            projectFile.setPropertyDefId(propertyDefId);
            projectFile = projectFileDao.save(projectFile);
            ProjectNode projectNodeCondition = new ProjectNode();
            projectNodeCondition.setProjectId(projectId);
            projectNodeCondition.setFlowNodeId(flowNodeId);
            ProjectNode projectNode = projectNodeDao.findOne(Example.of(projectNodeCondition)).orElse(null);
            ProjectNodeProperty projectNodeProperty = new ProjectNodeProperty();
            projectNodeProperty.setProjectId(projectId);
            projectNodeProperty.setFlowNodePropertyDefId(propertyDefId);
            if (Objects.nonNull(projectNode)) {
                ProjectNodeProperty existingProjectNodeProperty = projectNodePropertyDao.findOne(Example.of(projectNodeProperty)).orElse(null);
                if (Objects.nonNull(existingProjectNodeProperty)) {
                    projectNodeProperty.setFlowNodePropertyDefId(existingProjectNodeProperty.getFlowNodePropertyDefId());
                    projectNodeProperty = existingProjectNodeProperty;
                } else {
                    FlowNodeDefinition flowNodeDefinition = flowNodeDefinitionDao.getOne(propertyDefId);
                    projectNodeProperty.setProjectNodeId(flowNodeDefinition.getId());
                    projectNodeProperty.setProjectNodeId(projectNode.getId());
                }
                projectNodeProperty.setProjectId(projectId);
                projectNodeProperty.setProjectNodeId(projectNode.getId());
                projectNodeProperty.setFlowNodePropertyDefId(propertyDefId);
                projectNodeProperty.setPropertyValue(projectFile.getId() + "");
                projectNodePropertyDao.save(projectNodeProperty);
            }
            return projectFile.getId();
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            try {
                s3FileUtil.remove(bucketName, objectName);
            } catch (Exception ex) {
                throw getException(e);
            }
            throw getException(e);
        }
    }

    public void download(HttpServletResponse response, Long fileId) throws PmException {
        ProjectFile projectFile = projectFileDao.getOne(fileId);
        if (Objects.nonNull(projectFile)) {
            String bucketName = projectFile.getBucketName();
            String objectName = projectFile.getObjectName();
            String fileName = projectFile.getFileName();
            String fType = new MimetypesFileTypeMap().getContentType(fileName);
            response.reset();
            response.setHeader("content-type", fType + ";charset=utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try (GetObjectResponse getObjectResponse = s3FileUtil.getFile(bucketName, objectName)) {
                byte[] buf = new byte[1024];
                int len;
                try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                    while ((len=getObjectResponse.read(buf))!=-1){
                        os.write(buf,0,len);
                    }
                    os.flush();
                    byte[] bytes = os.toByteArray();
                    response.setCharacterEncoding("utf-8");
                    String UnicodeFileName = URLEncoder.encode(fileName, "UTF-8");
                    response.addHeader("Content-Disposition", "attachment;fileName=" + UnicodeFileName);
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

    public void download(Long projectId, Long flowNodeId, Long propertyDefId, HttpServletResponse response) throws PmException {
        ProjectFile projectFile = new ProjectFile();
        projectFile.setFlowNodeId(flowNodeId);
        projectFile.setProjectId(projectId);
        projectFile.setPropertyDefId(propertyDefId);
        projectFile = projectFileDao.findOne(Example.of(projectFile)).orElse(null);
        if (Objects.nonNull(projectFile)) {
            String bucketName = projectFile.getBucketName();
            String objectName = projectFile.getObjectName();
            String fileName = projectFile.getFileName();
            String fType = new MimetypesFileTypeMap().getContentType(fileName);
            response.reset();
            response.setHeader("content-type", fType + ";charset=utf-8");
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            try (GetObjectResponse getObjectResponse = s3FileUtil.getFile(bucketName, objectName)) {
                byte[] buf = new byte[1024];
                int len;
                try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                    while ((len=getObjectResponse.read(buf))!=-1){
                        os.write(buf,0,len);
                    }
                    os.flush();
                    byte[] bytes = os.toByteArray();
                    response.setCharacterEncoding("utf-8");
                    String UnicodeFileName = URLEncoder.encode(fileName, "UTF-8");
                    response.addHeader("Content-Disposition", "attachment;fileName=" + UnicodeFileName);
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
        // TODO We should process each exception here
        return pmException;
    }

}
