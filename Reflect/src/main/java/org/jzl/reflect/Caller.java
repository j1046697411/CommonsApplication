package org.jzl.reflect;

public interface Caller<R, T> {
    R call(T target, Object... args);
}
