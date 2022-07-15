package com.sxjkwm.pm.business.project.service;

import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author Vic.Chu
 * @date 2022/6/29 15:31
 */
@Service
public class ProjectFileService {

    private static final String patternBucketName = "pattern";

    private final ProjectService projectService;

    private final S3FileUtil s3FileUtil;

    @Autowired
    public ProjectFileService(ProjectService projectService, S3FileUtil s3FileUtil) {
        this.projectService = projectService;
        this.s3FileUtil = s3FileUtil;
    }

    public Boolean generateInquiryFile(Long projectId, Long flowNodeId, Long propertyDefinitionId) throws PmException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        ProjectDto projectDto = projectService.getId(projectId);
        if (Objects.isNull(projectDto)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        Integer projectTypeVal = projectDto.getProjectType();
        Constant.ProjectType projectType = Constant.ProjectType.getFromVal(projectTypeVal);
        String objectName = projectType.getObjectName();
        try (GetObjectResponse input = s3FileUtil.getFile(patternBucketName, objectName); XWPFDocument docxDocument = new XWPFDocument(input);) {
//            docxDocument.
        }

        return false;
    }

}
