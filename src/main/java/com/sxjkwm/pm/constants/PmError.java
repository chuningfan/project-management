package com.sxjkwm.pm.constants;

public enum PmError implements Constant<String, String> {

    /**
     * code
     * 1. starts with 100 means system error
     * 2. starts with 200 means third-party service error
     * 3. starts with 300 means data error
     * 4. starts with 400 means user operation error
     */
    // System error
    NO_SUCH_BEAN("1001", "Cannot find target bean"),
    // third-party error
    WXWORK_TOKEN_ERROR("2001", "Get WxWork token failed!"),
    WXWORK_LOGIN_FAILED("2002", "Login failed!"),
    WXWORK_READ_USER_FAILED("2003", "Cannot read user data from WxWork"),
    WXWORK_READ_DEPARTMENT_LIST_FAILED("2004", "Cannot read department list from WxWork"),
    WXWORK_SEND_MSG_FAILED("2005", "Send WxWork message failed"),

    NUONUO_GET_TOKEN_FAILED("2101", "Get NuoNuo token failed"),
    ;

    private String code;

    private String message;

    PmError(String code, String message) {
        this.code = code;
        this.message = message;
}

    @Override
    public String getValue() {
        return code;
    }

    @Override
    public String getLabel() {
        return message;
    }
}
