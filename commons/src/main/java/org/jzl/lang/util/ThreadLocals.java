package org.jzl.lang.util;

final class ThreadLocals {

    private static final int BUFF_SIZE = 1024 * 8;

    private static final ThreadLocal<char[]> CHARS_THREAD_LOCAL = ThreadLocal.withInitial(() -> new char[BUFF_SIZE]);
    private static final ThreadLocal<byte[]> BYTES_THREAD_LOCAL = ThreadLocal.withInitial(() -> new byte[BUFF_SIZE]);
    private static final ThreadLocal<int[]> INTS_THREAD_LOCAL = ThreadLocal.withInitial(() -> new int[BUFF_SIZE]);
    private static final ThreadLocal<long[]> LONGS_THREAD_LOCAL = ThreadLocal.withInitial(() -> new long[BUFF_SIZE]);


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

    private ThreadLocals() {
    }
}
