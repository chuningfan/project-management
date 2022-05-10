package com.sxjkwm.pm.common;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Observable;

public abstract class AbstractEventSource<T> extends Observable {

    protected AbstractEventSource() {
        List<PmListener<T>> listeners = listeners();
        if (CollectionUtils.isNotEmpty(listeners)) {
            for (PmListener<T> pmListener: listeners) {
                addObserver(pmListener);
            }
        }
    }

    protected abstract List<PmListener<T>> listeners();

    protected void pushEvent(T event) {
        setChanged();
        notifyObservers(event);
    }

}
