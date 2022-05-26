package com.sxjkwm.pm.exception;

import com.sxjkwm.pm.constants.Constant;
import com.sxjkwm.pm.constants.PmError;

public class PmException extends Exception {

    private String code;

    private String errorMessage;

    public PmException() {
    }

    public PmException(String errorMessage) {
        super(errorMessage);
        this.code = "500";
        this.errorMessage = errorMessage;
    }

    public PmException(Constant<String, String> error) {
        super(error.getLabel());
        this.errorMessage = error.getLabel();
        this.code = error.getValue();
    }

    public PmException(Constant<String, String> error, String msg) {
        super(error.getLabel() + ": " + msg);
        this.errorMessage = error.getLabel() + ": " + msg;
        this.code = error.getValue();
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public PmException appendMsg(String msg) {
        this.errorMessage = this.errorMessage + ": " + msg;
        return this;
    }

}
