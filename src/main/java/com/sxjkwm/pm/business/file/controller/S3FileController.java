package com.sxjkwm.pm.business.file.controller;

import com.sxjkwm.pm.business.file.service.S3FileService;
import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("/{projectId}/{flowNodeId}/{propertyKey}")
    public RestResponse<Long> upload(@RequestParam("file") MultipartFile file,
                                        @PathVariable("projectId") Long projectId,
                                        @PathVariable("flowNodeId") Long flowNodeId,
                                        @PathVariable("propertyKey") String propertyKey) throws PmException {
        return RestResponse.of(s3FileService.upload(projectId, file, flowNodeId, propertyKey));
    }

    @GetMapping("/file")
    public void downloadFileByKeys(HttpServletResponse response, @RequestParam("projectId") Long projectId, @RequestParam("flowNodeId") Long flowNodeId, @RequestParam("propertyKey") String propertyKey) throws PmException {
        s3FileService.download(projectId, flowNodeId, propertyKey, response);
    }

    @DeleteMapping("/{id}")
    public RestResponse<Boolean> deleteFileById(@PathVariable("id") Long id) throws PmException {
        return RestResponse.of(s3FileService.removeFileById(id));
    }

    @GetMapping("/{fileId}")
    public void downloadFileByFileId(HttpServletResponse response, @PathVariable("fileId") Long fileId) throws PmException {
        s3FileService.download(response, fileId);
    }

}
