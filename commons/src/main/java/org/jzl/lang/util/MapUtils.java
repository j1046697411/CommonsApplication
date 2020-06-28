package org.jzl.lang.util;

import org.jzl.lang.fun.BinaryConsumer;
import org.jzl.lang.util.map.FilterMap;
import org.jzl.lang.util.map.MapBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public final class MapUtils {

    private MapUtils() {
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean nonEmpty(Map<?, ?> map){
        return map != null && map.size() > 0;
    }

    public static <K, V> MapBuilder<K, V> of(BiPredicate<K, V> filter, Map<K, V> map) {
        return filter == null ? new MapBuilder<>(map) : new MapBuilder<>(new FilterMap<>(map, filter));
    }

    public static <K, V> MapBuilder<K, V> of() {
        return of(null, new HashMap<>());
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    public static <K, V> void each(BinaryConsumer<K, V> consumer){
        ObjectUtils.requireNonNull(consumer, "consumer");
    }

}
