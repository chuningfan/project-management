package com.sxjkwm.pm.logging;

import com.sxjkwm.pm.common.DoAfter;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LoggingAfterInvocation implements DoAfter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAfterInvocation.class);

    @Override
    public void doAfter(MethodSignature methodSignature, Object result, Object... args) {
        boolean hasArgs = Objects.nonNull(args) && args.length > 0;
        if (hasArgs) {
            logger.warn("Invoked {} with {}, result is {}", methodSignature.getDeclaringTypeName() + "#" + methodSignature.getName(), args, result);
        } else {
            logger.warn("Invoked {}, result is {}", methodSignature.getDeclaringTypeName() + "#" + methodSignature.getName(), result);
        }
    }
}
