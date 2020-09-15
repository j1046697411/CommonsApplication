package org.jzl.lang.util;

import org.jzl.lang.fun.Supplier;

final class ThreadLocals {

    private static final int BUFF_SIZE = 1024 * 8;

    private static final ThreadLocal<char[]> CHARS_THREAD_LOCAL = SupplierThreadLocal.of(() -> new char[BUFF_SIZE]);
    private static final ThreadLocal<byte[]> BYTES_THREAD_LOCAL = SupplierThreadLocal.of(() -> new byte[BUFF_SIZE]);
    private static final ThreadLocal<int[]> INTS_THREAD_LOCAL = SupplierThreadLocal.of(() -> new int[BUFF_SIZE]);
    private static final ThreadLocal<long[]> LONGS_THREAD_LOCAL = SupplierThreadLocal.of(() -> new long[BUFF_SIZE]);


    public static byte[] getBytes() {
        return BYTES_THREAD_LOCAL.get();
    }

    public static char[] getChars() {
        return CHARS_THREAD_LOCAL.get();
    }

    public static int[] getInts() {
        return INTS_THREAD_LOCAL.get();
    }

    public static long[] getLongs() {
        return LONGS_THREAD_LOCAL.get();
    }

    public static <T> ThreadLocal<T> of(Supplier<T> defSupplier) {
        return SupplierThreadLocal.of(defSupplier);
    }

    private ThreadLocals() {
    }

    private static class SupplierThreadLocal<T> extends ThreadLocal<T> {
        private Supplier<T> defSupplier;

        private SupplierThreadLocal(Supplier<T> defSupplier) {
            this.defSupplier = ObjectUtils.requireNonNull(defSupplier, "defSupplier");
        }

        @Override
        protected T initialValue() {
            return defSupplier.get();
        }

        public static <T> SupplierThreadLocal<T> of(Supplier<T> defSupplier) {
            return new SupplierThreadLocal<>(defSupplier);
        }

    }
}
