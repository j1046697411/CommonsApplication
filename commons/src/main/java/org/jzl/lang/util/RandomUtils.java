package org.jzl.lang.util;

import java.util.Random;

public final class RandomUtils {

    private static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    public static float random(float min, float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }

    public static float random() {
        return RANDOM.nextFloat();
    }

    public static int random(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static int random(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static float nextFloat(final float bound) {
        return RANDOM.nextFloat() * bound;
    }


}
