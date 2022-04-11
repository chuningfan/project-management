package com.sxjkwm.pm.business.file.service.impl;

import com.sxjkwm.pm.business.file.service.ProjectFileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class DefaultProjectFileServiceImpl implements ProjectFileService {
    @Override
    public Boolean upload(List<MultipartFile> files) {
        return null;
    }

    @Override
    public File get(String objId) {
        return null;
    }

    @Override
    public Boolean remove(String objId) {
        return null;
    }
}
