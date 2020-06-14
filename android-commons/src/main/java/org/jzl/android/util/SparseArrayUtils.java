package org.jzl.android.util;

import android.util.SparseArray;

import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.ObjectUtils;

public class SparseArrayUtils {

    private SparseArrayUtils() {
    }

    public static <E> void puts(SparseArray<E> array, E data, int... keys) {
        if (ArrayUtils.nonEmpty(keys)) {
            for (int key : keys) {
                array.put(key, data);
            }
        }
    }

    public static <E> void puts(SparseArray<E> array1, SparseArray<E> array2) {
        ObjectUtils.requireNonNull(array1);
        if (nonEmpty(array2)) {
            int size = array2.size();
            for (int i = 0; i < size; i++) {
                int key = array2.keyAt(i);
                E value = array2.valueAt(i);
                array1.put(key, value);
            }
        }
    }

    public static boolean isEmpty(SparseArray<?> array) {
        return array == null || array.size() == 0;
    }

    public static boolean nonEmpty(SparseArray<?> array) {
        return array != null && array.size() > 0;
    }

}
