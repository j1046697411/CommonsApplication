package org.jzl.lang.fun;

@FunctionalInterface
public interface Consumer<T> {
    void accept(T target);
}
