package com.sxjkwm.pm.logging;

import com.sxjkwm.pm.common.DoBefore;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingBeforeInvocation implements DoBefore {

    private static final Logger logger = LoggerFactory.getLogger(LoggingBeforeInvocation.class);

    @Override
    public void doBefore(MethodSignature signature, Object...args) {
        logger.warn("We are attempting invoke {} with args {}", signature.getDeclaringTypeName(), args);
    }
}
