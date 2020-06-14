package org.jzl.lang.fun;

public interface Function<T, R> {
    R apply(T target);
}
