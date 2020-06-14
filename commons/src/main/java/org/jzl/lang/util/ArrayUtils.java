package org.jzl.lang.util;

import org.jzl.lang.fun.IntConsumer;

import java.lang.reflect.Array;

public final class ArrayUtils {

    private ArrayUtils() {
    }

    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(short[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(byte[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(boolean[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(float[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(double[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean nonEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    public static boolean nonEmpty(int[] array) {
        return array != null && array.length > 0;
    }

    public static boolean isEmpty(Object array) {
        return array == null || array.getClass().isArray() || Array.getLength(array) == 0;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> type, int length) {
        ObjectUtils.requireNonNull(type);
        return (T[]) Array.newInstance(type, length);
    }

    public static int length(Object target) {
        return target == null ? 0 : Array.getLength(target);
    }

    public static <T> int length(T[] array) {
        return isEmpty(array) ? 0 : array.length;
    }

    public static boolean contains(int[] array, int value) {
        for (int k : array) {
            if (k == value) {
                return true;
            }
        }
        return false;
    }

    public static <T> void each(T[] array, IntConsumer<T> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        if (nonEmpty(array)) {
            for (int i = 0; i < array.length; i++) {
                consumer.accept(i, array[i]);
            }
        }
    }
}
