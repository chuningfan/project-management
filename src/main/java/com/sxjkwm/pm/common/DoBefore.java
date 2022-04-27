package com.sxjkwm.pm.common;

import org.aspectj.lang.reflect.MethodSignature;

public interface DoBefore {

    void doBefore(MethodSignature signature, Object...args);

}
