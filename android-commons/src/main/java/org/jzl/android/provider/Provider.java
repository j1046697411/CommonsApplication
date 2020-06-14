package org.jzl.android.provider;

import org.jzl.lang.util.ObjectUtils;

public interface Provider<T> {

    T provide();

    static <T> Provider<T> of(T content) {
        ObjectUtils.requireNonNull(content, "content");
        return () -> content;
    }
}
