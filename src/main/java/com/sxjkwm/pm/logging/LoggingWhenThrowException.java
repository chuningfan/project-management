package com.sxjkwm.pm.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingWhenThrowException {

    private static final Logger logger = LoggerFactory.getLogger(LoggingWhenThrowException.class);

    public void handleException(Throwable e) {
        logger.error(e.getCause().getMessage());
    }

}
