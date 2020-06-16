package org.jzl.lang.util.map;

import org.jzl.lang.util.MapUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;

public class FilterMap<K, V> implements Map<K, V> {

    private Map<K, V> map;
    private BiPredicate<K, V> filter;

    public FilterMap(Map<K, V> map, BiPredicate<K, V> filter) {
        this.map = Objects.requireNonNull(map);
        this.filter = Objects.requireNonNull(filter);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (filter.test(key, value)) {
            return value;
        }
        return map.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (MapUtils.isEmpty(m)) {
            return;
        }
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FilterMap) {
            return map.equals(((FilterMap) o).map);
        } else {
            return map.equals(o);
        }
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
