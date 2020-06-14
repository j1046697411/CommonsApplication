package org.jzl.lang.util;

public final class StringRandomUtils {

    private static final char[] NUMBERS_LETTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final char[] LOWER_CASE_LETTERS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final char[] UPPER_CASE_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private StringRandomUtils() {
    }

    public static String randomString(char[] chars, int length) {
        if (ArrayUtils.isEmpty(chars)) {
            return StringUtils.EMPTY;
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(chars[RandomUtils.random(chars.length)]);
        }
        return text.toString();
    }

    public static String randomNumber(int length) {
        return randomString(NUMBERS_LETTERS, length);
    }

    public static String randomUpperString(int length) {
        return randomString(UPPER_CASE_LETTERS, length);
    }

    public static String randomLowerString(int length) {
        return randomString(LOWER_CASE_LETTERS, length);
    }

}
