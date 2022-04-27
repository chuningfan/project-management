package com.sxjkwm.pm.logging;

import com.sxjkwm.pm.common.DoAfter;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAfterInvocation implements DoAfter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAfterInvocation.class);

    @Override
    public void doAfter(MethodSignature methodSignature, Object result) {
        logger.warn("Invoked {}, result is {}", methodSignature.getDeclaringTypeName() + "#" + methodSignature.getName(), result);
    }
}
