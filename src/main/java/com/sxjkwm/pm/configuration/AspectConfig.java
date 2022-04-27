package com.sxjkwm.pm.configuration;

import com.google.common.collect.Lists;
import com.sxjkwm.pm.common.DoAfter;
import com.sxjkwm.pm.common.DoBefore;
import com.sxjkwm.pm.logging.LoggingWhenThrowException;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Aspect
public class AspectConfig {

    private final List<DoBefore> beforeList = Lists.newArrayList();
    private final List<DoAfter> afterList = Lists.newArrayList();

    private final LoggingWhenThrowException loggingWhenThrowException;

    @Autowired
    public AspectConfig(LoggingWhenThrowException loggingWhenThrowException) {
        this.loggingWhenThrowException = loggingWhenThrowException;
    }

    @Around("@annotation(com.sxjkwm.pm.configuration.annotation.Valve)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        if (CollectionUtils.isNotEmpty(beforeList)) {
            for (DoBefore doBefore: beforeList) {
                doBefore.doBefore(methodSignature, args);
            }
        }
        Object result = null;
        try {
            if (args.length > 0) {
                result = proceedingJoinPoint.proceed(args);
            } else {
                result = proceedingJoinPoint.proceed();
            }
        } catch (Throwable e) {
            loggingWhenThrowException.handleException(e);
        }
        if (CollectionUtils.isNotEmpty(afterList)) {
            for (DoAfter doAfter: afterList) {
                doAfter.doAfter(methodSignature, result);
            }
        }
        return result;
    }

    public void addBeforeWorker(DoBefore before) {
        this.beforeList.add(before);
    }

    public void addAfterWorker(DoAfter after) {
        this.afterList.add(after);
    }

}
