package com.sxjkwm.pm.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public interface PmListener<T> extends Observer {

    Logger logger = LoggerFactory.getLogger(PmListener.class);

    @Override
    default void update(Observable o, Object arg) {
        if (Objects.nonNull(arg)) {
            try {
                T event = (T) arg;
                onEvent(event);
            } catch (Exception e) {
                logger.error("Listener error: {}", e.getMessage());
            }
        }
    }

    void onEvent(T event);

}
