package com.sxjkwm.pm.business.file.controller;

import com.sxjkwm.pm.business.file.service.PreviewService;
import com.sxjkwm.pm.configuration.annotation.PreviewFile;
import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Vic.Chu
 * @date 2022/5/28 7:28
 */
@RestController
@RequestMapping(Constant.API_FEATURE + "/preview")
public class PreviewController {

    private final PreviewService previewService;

    @Autowired
    public PreviewController(PreviewService previewService) {
        this.previewService = previewService;
    }

    @GetMapping
    @PreviewFile
    public void preview(@RequestParam("fileId") Long fileId, HttpServletRequest request, HttpServletResponse response) throws PmException {
        previewService.preview(fileId, response, true);
    }

    @GetMapping("/pattern")
    @PreviewFile
    public void previewPatternFile(@RequestParam("fileId") Long fileId, HttpServletRequest request, HttpServletResponse response) throws PmException {
        previewService.preview(fileId, response, false);
    }

}
