package org.jzl.lang.util;

import org.jzl.lang.fun.Supplier;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public final class ObjectUtils {
    private ObjectUtils() {
    }


    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean nonNull(Object object) {
        return object != null;
    }

    public static <T> T get(T target, T def) {
        return target == null ? def : target;
    }

    public static <T> T get(T target, Supplier<T> def) {
        ObjectUtils.requireNonNull(def, "Supplier");
        return target == null ? def.get() : target;
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof Collection<?>) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object instanceof CharSequence) {
            return ((CharSequence) object).length() == 0;
        } else {
            Class<?> type = object.getClass();
            if (type.isArray()) {
                return Array.getLength(object) == 0;
            }
        }
        return false;
    }

    public static boolean nonEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean equals(Object target1, Object target2) {
        return target1 != null && target1.equals(target2);
    }

    public static <T> T requireNonNull(T target, String message) {
        if (nonNull(target)) {
            return target;
        } else {
            throw new NullPointerException(message);
        }
    }

    public static <T> T requireNonNull(T target) {
        return requireNonNull(target, "");
    }
}
