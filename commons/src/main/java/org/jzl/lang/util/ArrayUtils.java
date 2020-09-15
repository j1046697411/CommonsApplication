package org.jzl.lang.util;

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

    public static boolean isEmpty(Object array) {
        return array == null || !array.getClass().isArray() || Array.getLength(array) == 0;
    }

    public static boolean nonEmpty(long[] array) {
        return array != null && array.length != 0;
    }

    public static boolean nonEmpty(short[] array) {
        return array != null && array.length != 0;
    }

    public static boolean nonEmpty(byte[] array) {
        return array != null && array.length != 0;
    }

    public static boolean nonEmpty(char[] array) {
        return array != null && array.length != 0;
    }

    public static boolean nonEmpty(boolean[] array) {
        return array != null && array.length != 0;
    }

    public static boolean nonEmpty(float[] array) {
        return array != null && array.length != 0;
    }

    public static boolean nonEmpty(double[] array) {
        return array != null && array.length != 0;
    }

    public static <T> boolean nonEmpty(T[] array) {
        return array != null && array.length > 0;
    }

    public static boolean nonEmpty(int[] array) {
        return array != null && array.length > 0;
    }


    @SuppressWarnings("unchecked")
    public static <T> T[] newArray(Class<T> type, int length) {
        return (T[]) Array.newInstance(ObjectUtils.requireNonNull(type, "type"), length);
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

    public static boolean contains(long[] array, long value) {
        if (nonEmpty(array)) {
            for (long l : array) {
                if (value == l) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(byte[] array, byte value) {
        if (nonEmpty(array)) {
            for (byte b : array) {
                if (value == b) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(short[] array, short value) {
        if (nonEmpty(array)) {
            for (short item : array) {
                if (value == item) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(char[] array, char value) {
        if (nonEmpty(array)) {
            for (char c : array) {
                if (value == c) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> boolean contains(T[] objects, T object, boolean identity) {
        if (nonEmpty(objects)) {
            if (identity) {
                for (T o : objects) {
                    if (o == object) {
                        return true;
                    }
                }
            } else {
                for (T o : objects) {
                    if (ObjectUtils.equals(o, object)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static <T> boolean contains(T[] objects, T object) {
        return contains(objects, object, false);
    }

}
