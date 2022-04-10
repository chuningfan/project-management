package com.sxjkwm.pm.common;

import java.io.Serializable;

public class RestResponse<T> implements Serializable {

    private static final String successful_code = "200";

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

    public static <T> RestResponse<T> get(T data) {
        return new RestResponse<T>().setData(data).setCode(successful_code);
    }

}
