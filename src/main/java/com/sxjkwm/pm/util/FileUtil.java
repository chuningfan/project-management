package com.sxjkwm.pm.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;

public class FileUtil {

    public static String upload(MultipartFile file, String dir) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String filePath = dir.replace("/", File.separator) + File.separator + originalFileName;
        File fileFullPath = new File(filePath);
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            if (fileFullPath.exists()) {
                fileFullPath.delete();
            }
            fileFullPath.createNewFile();
            inputStream = file.getInputStream();
            fileOutputStream = new FileOutputStream(fileFullPath);
            int length = 0;
            byte[] buffer = new byte[2048];
            while ((length = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
            return fileFullPath.getPath();
        } finally {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
            if (Objects.nonNull(fileOutputStream)) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
    }

    public static void deleteFile(Collection<File> files) {
        if (CollectionUtils.isNotEmpty(files)) {
            for(File file: files) {
                if (file.exists()) file.delete();
            }
        }
    }

    public static void deleteFileByPath(Collection<String> filePaths) {
        if (CollectionUtils.isNotEmpty(filePaths)) {
            File filePath;
            for (String path: filePaths) {
                filePath = new File(path);
                if (filePath.exists()) {
                    filePath.delete();
                }
            }
        }
    }

}
