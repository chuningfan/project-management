package com.sxjkwm.pm.logging;

import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LoggingWhenThrowException {

    private static final Logger logger = LoggerFactory.getLogger(LoggingWhenThrowException.class);

    public void handleException(MethodSignature signature, Throwable e, Object...args) {
        boolean hasArgs = (Objects.nonNull(args) && args.length > 0);
        if (hasArgs) {
            logger.error("When invoking {} with {}, an error occurred: {} ", signature.getDeclaringType() + "#" + signature.getName(), args, e.getCause().getMessage());
        } else {
            logger.error("When invoking {}, an error occurred: {} ", signature.getDeclaringType() + "#" + signature.getName(), e.getCause().getMessage());
        }
    }

}
