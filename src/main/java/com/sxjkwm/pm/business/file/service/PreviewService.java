package com.sxjkwm.pm.business.file.service;

import com.google.common.collect.Sets;
import com.sxjkwm.pm.auth.context.impl.ContextHelper;
import com.sxjkwm.pm.auth.dto.UserDataDto;
import com.sxjkwm.pm.business.file.dao.ProjectFileDao;
import com.sxjkwm.pm.business.file.entity.PatternFile;
import com.sxjkwm.pm.business.file.entity.ProjectFile;
import com.sxjkwm.pm.business.project.dto.ProjectDto;
import com.sxjkwm.pm.business.project.service.ProjectService;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.FileUtil;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
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

    private final ProjectService projectService;

    private final PatternFileService patternFileService;

    @Autowired
    public PreviewService(ProjectFileDao projectFileDao, S3FileUtil s3FileUtil, ProjectService projectService, PatternFileService patternFileService) {
        this.projectFileDao = projectFileDao;
        this.s3FileUtil = s3FileUtil;
        this.projectService = projectService;
        this.patternFileService = patternFileService;
    }

    public void preview(Long fileId, HttpServletResponse response, boolean isProjectFile) throws PmException {
        String fileName = null;
        String bucketName = null;
        String objectId = null;
        if (isProjectFile) {
            ProjectFile projectFile = projectFileDao.getOne(fileId);
            if (Objects.isNull(projectFile)) {
                throw new PmException(PmError.NO_DATA_FOUND);
            }
            ProjectDto projectDto = projectService.getId(projectFile.getProjectId());
            if (Objects.isNull(projectDto)) {
                throw new PmException(PmError.NO_DATA_FOUND);
            }
            String ownerUserId = projectDto.getOwnerUserId();
            UserDataDto currentUser = ContextHelper.getUserData();
            List<String> roleNames = currentUser.getRoleNames();
            if (CollectionUtils.isNotEmpty(roleNames) && roleNames.contains("业务人员")) {
                String currentUserId = currentUser.getWxUserId();
                if (!ownerUserId.equals(currentUserId)) {
                    throw new PmException(PmError.NO_PRIVILEGES);
                }
            }
            fileName = projectFile.getFileName();
            bucketName = projectFile.getBucketName();
            objectId = projectFile.getObjectName();
        } else {
            PatternFile patternFile = patternFileService.findById(fileId);
            if (Objects.isNull(patternFile)) {
                throw new PmException(PmError.NO_PRIVILEGES);
            }
            fileName = patternFile.getFileName();
            bucketName = patternFile.getBucketName();
            objectId = patternFile.getObjName();
        }
        String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if ("xlsx".equals(fileSuffix)) {
            fileSuffix = "xls";
        }
        if ("docx".equals(fileSuffix)) {
            fileSuffix = "doc";
        }
        if ("pdf".equals(fileSuffix.toLowerCase())) {
            try (GetObjectResponse objectResponse = s3FileUtil.getFile(bucketName, objectId);
                 OutputStream outputStream = response.getOutputStream()) {
                byte[] buff = new byte[1024];
                int n;
                while ((n = objectResponse.read(buff)) != -1) {
                    outputStream.write(buff, 0, n);
                }
                outputStream.flush();
            } catch (Exception e) {
                throw new PmException(PmError.INTERNAL_ERROR);
            }
        } else {
            if (!supportedSuffixes.contains(fileSuffix)) {
                throw new PmException(PmError.FILE_PREVIEW_NOT_SUPPORTED);
            }
            try (GetObjectResponse objectResponse = s3FileUtil.getFile(bucketName, objectId);
                 InputStream convertedStream = FileUtil.covertCommonByStream(objectResponse, fileSuffix);
                 OutputStream outputStream = response.getOutputStream();) {
                byte[] buff = new byte[1024];
                int n;
                while ((n = convertedStream.read(buff)) != -1) {
                    outputStream.write(buff, 0, n);
                }
                outputStream.flush();
            } catch (Exception e) {
                throw new PmException(PmError.INTERNAL_ERROR);
            }
        }
    }

}
