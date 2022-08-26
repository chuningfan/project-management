package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionConfig {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionConfig.class);

    @ExceptionHandler({Exception.class})
    public RestResponse handle(Exception e) {
        RestResponse restResponse = new RestResponse();
        if (Objects.isNull(e)) {
            restResponse.setCode("500").setMessage("NullPointException");
        } else if (e instanceof PmException) {
            PmException pmException = (PmException) e;
            restResponse.setCode(pmException.getCode()).setMessage(pmException.getErrorMessage());
        } else {
            restResponse.setCode("500").setMessage("Exception: " + e.getMessage());
        }
        logger.error("PM-ERROR: {}", restResponse);
        return restResponse;
    }

}
