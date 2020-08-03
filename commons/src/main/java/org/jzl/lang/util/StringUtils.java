package org.jzl.lang.util;

import org.jzl.lang.fun.Function;

import java.util.Collection;
import java.util.Map;

/**
 * 个人常用的java StringUtils 工具类
 */

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

    public static String trimIfEmpty(String text) {
        return text == null ? EMPTY : text.trim();
    }

    public static String toUpperCaseIfEmpty(String text) {
        return text == null ? EMPTY : text.toUpperCase();
    }

    public static String toLowerCaseIfEmpty(String text) {
        return text == null ? EMPTY : text.toLowerCase();
    }

    public static String toIfEmpty(String text, String def, Function<String, String> mapper) {
        return text == null ? def : ObjectUtils.get(mapper.apply(text), def);
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
        char[] chars = new char[2];
        char[] hexChars = isUpper ? UPPER_HEX_CHARS : LOWER_HEX_CHARS;
        for (byte b : bytes) {
            int value = b & 0xff;
            chars[0] = hexChars[value >> 4];
            chars[1] = hexChars[value & 0x0f];
            joiner.join(chars, 0, 2);
        }
        return joiner.toString();
    }

    public static String toUpperHexString(byte[] bytes) {
        return toHexString(EMPTY, bytes, true);
    }

    public static String toLowerHexString(byte[] bytes) {
        return toHexString(EMPTY, bytes, false);
    }
}
