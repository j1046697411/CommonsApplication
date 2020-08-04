package org.jzl.lang.util;

import org.jzl.lang.fun.Function;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人常用的java StringUtils 工具类
 */

public final class StringUtils {

    private static final char[] LOWER_HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] UPPER_HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String PATTERN_NUMBER_16 = "^#|(0[xX])[0-9a-fA-F]+$";
    public static final String PATTERN_NUMBER_8 = "^(\\+|\\-?)0[0-7]*$";
    public static final String PATTERN_NUMBER_10 = "^(\\+|\\-?)(0|([1-9][0-9]*))$";


    public static final String[] EMPTY_ARRAY = new String[0];
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

    public static String[] splitIfEmptyArray(String text, String regex) {
        return text == null ? EMPTY_ARRAY : text.split(regex);
    }

    public static String replaceIfEmpty(String text, CharSequence regex, CharSequence replacement) {
        return text == null ? EMPTY : text.replace(regex, replacement);
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

    public static boolean matches(String text, String regex) {
        return text != null && Pattern.matches(regex, text);
    }

    public static String findIfEmpty(String text, String regex, int index, int group) {
        if (StringUtils.nonEmpty(text) && StringUtils.nonEmpty(regex)) {
            Matcher matcher = Pattern.compile(regex).matcher(text);
            if (matcher.find(index)) {
                return matcher.group(group);
            }
        }
        return EMPTY;
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
