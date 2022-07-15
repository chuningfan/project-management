package com.sxjkwm.pm.constants;

import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSONObject;
import com.sxjkwm.pm.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ErrorPage {

    private static final Logger logger = LoggerFactory.getLogger(ErrorPage.class);

    private static final String errorPage = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>PM</title></head><body>Sorry:%s</body></html>";

    public static String getErrorPageHtml(String errorMessage) {
        return String.format(errorPage, errorMessage);
    }

    public static void writeErrorAsJson(HttpServletResponse response, Exception e) {
        response.setContentType(ContentType.JSON.getValue());
        response.setStatus(200);
        RestResponse restResponse = RestResponse.of(e);
        String jsonString = JSONObject.toJSONString(restResponse);
        try (Writer writer = response.getWriter()) {
            writer.write(jsonString);
        } catch (IOException ex) {
            logger.error("Getting writer failed: {}", e);
        }
    }

}
