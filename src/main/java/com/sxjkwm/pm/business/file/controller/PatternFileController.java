package com.sxjkwm.pm.business.file.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sxjkwm.pm.business.file.dto.FileCategoryAndType;
import com.sxjkwm.pm.business.file.handler.replacement.BaseReplacement;
import com.sxjkwm.pm.business.file.service.PatternFileService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patternFile")
public class PatternFileController {

    private final PatternFileService patternFileService;

    @Autowired
    public PatternFileController(PatternFileService patternFileService) {
        this.patternFileService = patternFileService;
    }

    @PostMapping("/{fileCategory}/{fileType}")
    public RestResponse<String> upload(@RequestParam MultipartFile file,
                                       @PathVariable("fileCategory") Integer fileCategory, @PathVariable("fileType") Integer fileType) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return RestResponse.of(patternFileService.upload(file, fileCategory, fileType));
    }

    @DeleteMapping("/{patternFileId}")
    public RestResponse<Boolean> deleteFile(@PathVariable("patternFileId") Long patternFileId) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return RestResponse.of(patternFileService.remove(patternFileId));
    }

    @PostMapping("/production/{patternFileId}/{projectId}/{flowNodeId}/{propertyDefId}")
    public RestResponse<Long> generate(@PathVariable("patternFileId") Long patternFileId,
                                       @PathVariable("projectId") Long projectId,
                                       @PathVariable("flowNodeId") Long flowNodeId,
                                       @PathVariable("propertyDefId") Long propertyDefId) throws Throwable {
        return RestResponse.of(patternFileService.generateFile(patternFileId, projectId, flowNodeId, propertyDefId));
    }

    @GetMapping("/categoryAndType")
    public RestResponse<List<FileCategoryAndType>> fetchCategories() {
        List<Constant.FileType> types = Lists.newArrayList(Arrays.asList(Constant.FileType.values()));
        List<FileCategoryAndType> categoryAndTypes = Lists.newArrayList();
        FileCategoryAndType categoryAndType;
        for (Constant.FileType type: types) {
            categoryAndType = new FileCategoryAndType();
            categoryAndType.setCategoryId(type.getCategory());
            categoryAndType.setCategoryName(type.getCategoryName());
            categoryAndType.setFileTypeId(type.getValue());
            categoryAndType.setFileTypeName(type.getLabel());
            categoryAndTypes.add(categoryAndType);
        }
        return RestResponse.of(categoryAndTypes);
    }



    @GetMapping("/files")
    public RestResponse<Map<Long, String>> fetchFilesByCategory(@RequestParam("category") Integer category) {
        return RestResponse.of(patternFileService.fetchPatternFilesByCategory(category));
    }

}
