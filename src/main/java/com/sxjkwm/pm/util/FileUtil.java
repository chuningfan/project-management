package com.sxjkwm.pm.util;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Objects;

public class FileUtil {

    private static final String DEFAULT_SUFFIX = "pdf";

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

    public static void download(HttpServletResponse resp, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        String fileName = file.getName();
        String type = new MimetypesFileTypeMap().getContentType(fileName);
        resp.reset();
        resp.setHeader("content-type", type + ";charset=utf-8");
        resp.setContentType("application/octet-stream;charset=UTF-8");
        resp.addHeader("Content-Length", String.valueOf(file.length()));
        resp.setCharacterEncoding("UTF-8");
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os;
        try {
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            resp.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
            os = resp.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static InputStream covertCommonByStream(InputStream inputStream, String suffix) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DocumentConverter converter = ContextUtil.getBean(DocumentConverter.class);
        DefaultDocumentFormatRegistry formatReg = new DefaultDocumentFormatRegistry();
        DocumentFormat targetFormat = formatReg.getFormatByFileExtension(DEFAULT_SUFFIX);
        DocumentFormat sourceFormat = formatReg.getFormatByFileExtension(suffix);
        converter.convert(inputStream, sourceFormat, out, targetFormat);
//        connection.disconnect();
        return outputStreamConvertInputStream(out);
    }

    public static ByteArrayInputStream outputStreamConvertInputStream(final OutputStream out) throws Exception {
        ByteArrayOutputStream baos=(ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
