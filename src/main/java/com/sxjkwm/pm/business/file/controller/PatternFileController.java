package com.sxjkwm.pm.business.file.controller;

import com.sxjkwm.pm.business.file.service.PatternFileService;
import com.sxjkwm.pm.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/patternFile")
public class PatternFileController {

    private final PatternFileService patternFileService;

    @Autowired
    public PatternFileController(PatternFileService patternFileService) {
        this.patternFileService = patternFileService;
    }

    @PostMapping
    public RestResponse<List<String>> update(@RequestParam("files") List<MultipartFile> files) {
        return RestResponse.of(patternFileService.upload(files));
    }

}
