package com.sxjkwm.pm.auth.context;

public interface ContextFactory<T> {

    Context<T> get();

    void remove();

}
