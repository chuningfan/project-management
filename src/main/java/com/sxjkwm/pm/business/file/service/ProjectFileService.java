package com.sxjkwm.pm.business.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ProjectFileService {

    Boolean upload(List<MultipartFile> files);

    File get(String objId);

    Boolean remove(String objId);

}
