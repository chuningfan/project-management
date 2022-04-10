package com.sxjkwm.pm.exception;

import com.sxjkwm.pm.constants.PmError;

public class PmException extends Exception {

    private String code;

    private String errorMessage;

    public PmException() {
    }

    public PmException(PmError pmError) {
        this.errorMessage = pmError.getLabel();
        this.code = pmError.getValue();
    }

    public PmException(PmError pmError, String msg) {
        this.code = pmError.getValue();
        this.errorMessage = pmError.getLabel() + ": " + msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
