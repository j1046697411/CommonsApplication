package org.jzl.lang.util;

public class MathUtils {

    private MathUtils() {
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(value, max));
    }

    public static boolean isLimit(long value, long min, long max) {
        return value >= min && value <= max;
    }

    public static boolean isLimit(float value, float min, float max) {
        return value >= min && value <= max;
    }

    public static boolean isLimit(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isLimit(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean equal(float value1, float value2, float offsetValue){
        return Math.abs(value1 - value2) <= offsetValue;
    }

    public static boolean equal(double value1, double value2, double offsetValue){
        return Math.abs(value1 - value2) <= offsetValue;
    }
}
