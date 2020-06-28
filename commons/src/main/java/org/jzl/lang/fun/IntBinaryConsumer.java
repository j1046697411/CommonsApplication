package org.jzl.lang.fun;

@FunctionalInterface
public interface IntBinaryConsumer {
    void accept(int index, int target);
}
