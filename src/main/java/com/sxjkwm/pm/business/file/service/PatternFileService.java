package com.sxjkwm.pm.business.file.service;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.configuration.SystemConfig;
import com.sxjkwm.pm.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PatternFileService {

    private final SystemConfig systemConfig;

    private final String patternFilePath;

    @Autowired
    public PatternFileService(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
        patternFilePath = systemConfig.getPatternFilePath();
        File file = new File(patternFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public List<String> upload(List<MultipartFile> files) {
        List<String> uploadedList = Lists.newArrayList();
        for (MultipartFile file: files) {
            try {
                uploadedList.add(FileUtil.upload(file, patternFilePath));
            } catch (IOException e) {
                FileUtil.deleteFileByPath(uploadedList);
                return Collections.emptyList();
            }
        }
        return uploadedList;
    }

    public File get(String path) {
        File target = new File(path);
        if (!target.exists()) {
            return null;
        }
        return target;
    }

    public Boolean remove(String path) {
        File target = get(path);
        if (Objects.nonNull(target)) {
            return target.delete();
        }
        return false;
    }

}
