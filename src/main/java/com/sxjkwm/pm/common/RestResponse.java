package com.sxjkwm.pm.common;

import com.sxjkwm.pm.exception.PmException;

import java.io.Serializable;

public class RestResponse<T> implements Serializable {

    private static final String successful_code = "200";

    private static final String error_code = "500";

    private T data;

    private String code;

    private String message;

    public T getData() {
        return data;
    }

    public RestResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RestResponse<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RestResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public static <T> RestResponse<T> of(T data) {
        return new RestResponse<T>().setData(data).setCode(successful_code);
    }

    public static RestResponse of(Exception e) {
        RestResponse response = new RestResponse();
        if (e instanceof PmException) {
            PmException pmException = (PmException) e;
            response.setCode(pmException.getCode())
            .setMessage(pmException.getErrorMessage());
        } else {
            response.setCode(error_code).setMessage(e.getMessage());
        }
        return response;
    }

}
