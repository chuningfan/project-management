package com.sxjkwm.pm.function.dto;

import com.sxjkwm.pm.function.entity.Function;

import java.io.Serializable;

/**
 * @author Vic.Chu
 * @date 2022/8/1 14:17
 */
public class FunctionDto implements Serializable {

    private String functionName;

    private String uri;

    public FunctionDto() {
    }

    public FunctionDto(Function function) {
        this.functionName = function.getFunctionName();
        this.uri = function.getUri();
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
