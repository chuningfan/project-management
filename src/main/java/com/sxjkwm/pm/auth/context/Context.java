package com.sxjkwm.pm.auth.context;

public interface Context<T> {

    Context<T> of(T t);

    T unwrap();

}
