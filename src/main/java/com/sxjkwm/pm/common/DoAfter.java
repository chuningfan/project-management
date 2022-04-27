package com.sxjkwm.pm.common;

import org.aspectj.lang.reflect.MethodSignature;

public interface DoAfter {

    void doAfter(MethodSignature methodSignature, Object result);

}
