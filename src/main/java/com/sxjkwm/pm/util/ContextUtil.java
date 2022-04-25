package com.sxjkwm.pm.util;


import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

public class ContextUtil {

    public static ApplicationContext context;

    public static <T> T getBean(String name) {
        if (Objects.isNull(context)) return null;
        return (T) context.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        if (Objects.isNull(context)) return null;
        return context.getBean(clazz);
    }

    public static ListableBeanFactory getBeanFactory() {
        if (Objects.isNull(context)) return null;
         return context;
    }

}
