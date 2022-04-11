package com.sxjkwm.pm.constants;

public class ErrorPage {

    private static final String errorPage = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><title>PM</title></head><body>Sorry:%s</body></html>";

    public static String getErrorPageHtml(String errorMessage) {
        return String.format(errorPage, errorMessage);
    }

}
