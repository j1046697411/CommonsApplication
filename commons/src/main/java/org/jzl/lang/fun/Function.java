package org.jzl.lang.fun;

@FunctionalInterface
public interface Function<T, R> {
    R apply(T target);
}
