package com.sxjkwm.pm.business.file.controller;

import com.sxjkwm.pm.business.file.service.PreviewService;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Vic.Chu
 * @date 2022/5/28 7:28
 */
@RestController
@RequestMapping("/preview")
public class PreviewController {

    private final PreviewService previewService;

    @Autowired
    public PreviewController(PreviewService previewService) {
        this.previewService = previewService;
    }

    @GetMapping
    public void preview(@RequestParam("fileId") Long fileId, HttpServletResponse response) throws PmException {
        previewService.preview(fileId, response, true);
    }

    @GetMapping("/pattern")
    public void previewPatternFile(@RequestParam("fileId") Long fileId, HttpServletResponse response) throws PmException {
        previewService.preview(fileId, response, false);
    }

}
