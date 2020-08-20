package org.jzl.lang.util.datablcok;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface DataSource<T> extends List<T> {

    void move(int fromPosition, int toPosition);

    void replaceAll(Collection<T> newData);

    @SuppressWarnings("all")
    default void addAll(T... data) {
        addAll(Arrays.asList(data));
    }

    @SuppressWarnings("all")
    default void addAll(int index, T... data) {
        addAll(index, Arrays.asList(data));
    }

    List<T> snapshot();

}
