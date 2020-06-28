package org.jzl.lang.fun;

@FunctionalInterface
public interface IntConsumer<T> {
    void accept(int index, T target);
}
