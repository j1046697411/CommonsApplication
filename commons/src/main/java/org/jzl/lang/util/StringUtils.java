package org.jzl.lang.util;

import java.util.Collection;
import java.util.Map;

public final class StringUtils {
    private static final char[] LOWER_HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] UPPER_HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


    public static final String EMPTY = "";
    public static final String SPACE = " ";

    private StringUtils() {
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean nonEmpty(CharSequence text) {
        return text != null && text.length() > 0;
    }

    public static int length(CharSequence text) {
        return text == null ? 0 : text.length();
    }

    @SafeVarargs
    public static <T> String join(CharSequence delimiter, T... texts) {
        return joiner(delimiter).joins(texts).toString();
    }

    public static <T> String join(CharSequence delimiter, Collection<T> texts) {
        return joiner(delimiter).joins(texts).toString();
    }

    public static <K, V> String join(CharSequence delimiter, String mapDelimiter, Map<K, V> map) {
        return joiner(delimiter).joins(mapDelimiter, map).toString();
    }

    public static StringJoiner joiner(CharSequence prefix, CharSequence delimiter, CharSequence suffix) {
        return new StringJoiner(prefix, delimiter, suffix);
    }

    public static StringJoiner joiner(CharSequence delimiter) {
        return new StringJoiner(delimiter);
    }

    public static String toHexString(int i) {
        return Integer.toHexString(i);
    }

    public static String toHexString(String delimiter, byte[] bytes, boolean isUpper) {
        StringJoiner joiner = joiner(delimiter);
        char[] chars = ThreadLocals.getChars();
        char[] hexChars = isUpper ? UPPER_HEX_CHARS : LOWER_HEX_CHARS;
        for (byte b : bytes) {
            int value = b & 0xff;
            chars[0] = hexChars[value >> 4];
            chars[1] = hexChars[value & 0x0f];
            joiner.join(chars, 0, 2);
        }
        return joiner.toString();
    }

    public static String toHexString(byte[] bytes) {
        return toHexString(EMPTY, bytes, true);
    }
}
