package com.sxjkwm.pm.business.file.service;

import cn.hutool.core.lang.UUID;
import com.google.common.collect.Lists;
import com.sxjkwm.pm.business.file.dao.PatternFileDao;
import com.sxjkwm.pm.business.file.dto.PatternFileDto;
import com.sxjkwm.pm.business.file.entity.PatternFile;
import com.sxjkwm.pm.business.file.handler.PatternFileHandler;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;
import com.sxjkwm.pm.exception.PmException;
import com.sxjkwm.pm.util.ContextUtil;
import com.sxjkwm.pm.util.PinYinUtil;
import com.sxjkwm.pm.util.S3FileUtil;
import io.minio.GetObjectResponse;
import io.minio.errors.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PatternFileService {

    private final S3FileUtil s3FileUtil;

    private final PatternFileDao patternFileDao;

    @Autowired
    public PatternFileService(S3FileUtil s3FileUtil, PatternFileDao patternFileDao) {
        this.s3FileUtil = s3FileUtil;
        this.patternFileDao = patternFileDao;
    }

    public PatternFile findById(Long patternFileId) {
        return patternFileDao.findById(patternFileId).orElse(null);
    }

    /**
     * 上传模板文件，为将来自动生成合同做准备
     *
     * @return
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws XmlParserException
     * @throws InternalException
     */
    public String upload(MultipartFile file, Integer fileCategory, Integer fileType) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String bucketName  = "pattern";
        String objectName = fileCategory + "/" + (UUID.fastUUID().toString().replace("-", "").toLowerCase());
        s3FileUtil.upload(bucketName, objectName, file);
        PatternFile patternFile = new PatternFile();
        patternFile.setObjName(objectName);
        patternFile.setSuffix(suffix);
        patternFile.setFileName(fileName);
        patternFile.setBucketName(bucketName);
        patternFile.setIsDeleted(0);
        patternFile.setFileCategory(fileCategory);
        patternFile.setFileType(fileType);
        patternFileDao.save(patternFile);
        return objectName;
    }


    public Boolean remove(Long patternFileId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        PatternFile patternFile = patternFileDao.getOne(patternFileId);
        if (Objects.nonNull(patternFile)) {
            patternFile.setIsDeleted(1);
            patternFileDao.save(patternFile);
            String bucketName = patternFile.getBucketName();
            String objName = patternFile.getObjName();
            s3FileUtil.remove(bucketName, objName);
            return true;
        }
        return false;
    }


    public Long generateFile(Long patternFileId, Long dataId, Long flowNodeId, Long propertyDefId) throws Throwable {
        PatternFile patternFile = patternFileDao.getOne(patternFileId);
        if (Objects.isNull(patternFile)) {
            throw new PmException(PmError.NO_DATA_FOUND);
        }
        try (GetObjectResponse fileResponse = s3FileUtil.getFile(patternFile.getBucketName(), patternFile.getObjName());) {
            if (Objects.isNull(fileResponse)) {
                throw new PmException(PmError.NO_DATA_FOUND);
            }
            Constant.FileType fileType = Constant.FileType.getFromVal(patternFile.getFileCategory(), patternFile.getFileType());
            PatternFileHandler patternFileHandler = ContextUtil.getBean(fileType.fileHandlerClass());
            return patternFileHandler.doHandle0(fileResponse, dataId, flowNodeId, propertyDefId, patternFile.getFileName());
        }
    }

    public List<PatternFileDto> fetchPatternFilesByCategory(Integer category) {
        PatternFile condition = new PatternFile();
        if (Objects.nonNull(category)) {
            condition.setFileCategory(category);
        }
        condition.setIsDeleted(0);
        List<PatternFile> files = patternFileDao.findAll(Example.of(condition));
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        }
        List<PatternFileDto> patternFileDtos = Lists.newArrayList();
        PatternFileDto dto;
        for (PatternFile file: files) {
            Integer categoryId = file.getFileCategory();
            Integer typeId = file.getFileType();
            Constant.FileType fileType = Constant.FileType.getFromVal(categoryId, typeId);
            dto = new PatternFileDto(file.getId(), file.getFileName(), categoryId, fileType.getCategoryName(), fileType.getValue(), fileType.getLabel());
            patternFileDtos.add(dto);
        }
        return patternFileDtos;
    }


}
