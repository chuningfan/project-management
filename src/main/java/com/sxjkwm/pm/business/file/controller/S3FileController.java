package com.sxjkwm.pm.business.file.controller;

import com.sxjkwm.pm.business.file.service.S3FileService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.constants.Constant;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/**
 * @author Vic.Chu
 * @date 2022/5/12 20:59
 */

@RestController
@RequestMapping("/s3")
public class S3FileController {

    private final S3FileService s3FileService;

    @Autowired
    public S3FileController(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }


    @PostMapping("/{projectId}/{nodeId}/{fileType}")
    public RestResponse<Long> upload(@RequestParam("file") MultipartFile file,
                                        @PathVariable("projectId") Long projectId,
                                        @PathVariable("nodeId") Long nodeId,
                                        @PathVariable("fileType") String fileType,
                                        @RequestParam("fileName") String fileName) {
        return RestResponse.of(s3FileService.upload(projectId, file, Constant.FileType.valueOf(fileType.toUpperCase(Locale.ROOT)), nodeId, fileName));
    }

    @GetMapping("/{projectId}/{nodeId}/{fileType}")
    public void download(HttpServletResponse response, @PathVariable("projectId") Long projectId, @PathVariable("nodeId") Long nodeId, @PathVariable("fileType") String fileType) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        s3FileService.download(projectId, nodeId, fileType, response);
    }

}
