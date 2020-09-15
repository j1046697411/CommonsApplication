package org.jzl.lang.util;

public final class StringRandomUtils {

    private static final char[] NUMBERS_LETTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final char[] LOWER_CASE_LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] UPPER_CASE_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '_'
    };


    private StringRandomUtils() {
    }

    public static String randomNumber(int length) {
        return randomString(length, NUMBERS_LETTERS);
    }

    public static String randomUpperString(int length) {
        return randomString(length, UPPER_CASE_LETTERS);
    }

    public static String randomLowerString(int length) {
        return randomString(length, LOWER_CASE_LETTERS);
    }

    public static String randomString(int length) {
        return randomString(length, CHARS);
    }

    public static String randomString(int length, char... chars) {
        if (ArrayUtils.isEmpty(chars) || length <= 0) {
            return StringUtils.EMPTY;
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(chars[RandomUtils.random(chars.length)]);
        }
        return text.toString();
    }

}
