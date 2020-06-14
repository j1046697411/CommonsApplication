package org.jzl.android.recyclerview.wrap;

import android.util.SparseArray;

import org.jzl.lang.util.ObjectUtils;

public class CommonlyWrapper<E> implements EntityWrapper<E> {

    private E entity;
    private SparseArray<Object> params;

    public CommonlyWrapper(E entity) {
        this.entity = entity;
    }

    @Override
    public E wrap() {
        return entity;
    }

    @Override
    public void put(int key, Object value) {
        if (ObjectUtils.isNull(params)) {
            this.params = new SparseArray<>();
        }
        if (ObjectUtils.nonNull(value)) {
            params.put(key, value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(int key, T def) {
        if (ObjectUtils.nonNull(params)) {
            return (T) params.get(key, def);
        } else {
            return def;
        }
    }
}
