package com.sxjkwm.pm.configuration;

import com.sxjkwm.pm.common.RestResponse;
import com.sxjkwm.pm.exception.PmException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler({Exception.class})
    public RestResponse handle(Exception e) {
        RestResponse restResponse = new RestResponse();
        if (Objects.isNull(e)) {
            return restResponse.setCode("500").setMessage("NullPointException");
        } else if (e instanceof PmException) {
            PmException pmException = (PmException) e;
            return restResponse.setCode(pmException.getCode()).setMessage(pmException.getErrorMessage());
        } else {
            return restResponse.setCode("500").setMessage("Unknown exception, please check logs");
        }
    }

}
