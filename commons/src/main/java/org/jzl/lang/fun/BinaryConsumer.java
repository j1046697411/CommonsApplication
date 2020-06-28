package org.jzl.lang.fun;

@FunctionalInterface
public interface BinaryConsumer<T, U> {
    void apply(T t, U u);
}
