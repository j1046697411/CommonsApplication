package org.jzl.android.recyclerview.wrap;

public interface EntityWrapper<E> {
    E wrap();

    void put(int key, Object value);

    <T> T get(int key, T def);

    default  <T> T get(int key){
        return get(key, null);
    }
}
