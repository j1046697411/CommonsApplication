package org.jzl.lang.util.map;

import org.jzl.lang.builder.Builder;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> implements Builder<Map<K, V>> {

    private Map<K, V> map;

    public MapBuilder() {
        this(new HashMap<>());
    }

    public MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> putAll(Map<K, V> map) {
        this.map.putAll(map);
        return this;
    }

    @Override
    public Map<K, V> build() {
        return map;
    }
}
