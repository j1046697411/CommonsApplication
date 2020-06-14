package org.jzl.android.recyclerview.fun;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.Provider;
import org.jzl.lang.fun.IntConsumer;

import java.util.Collection;
import java.util.List;

public interface DataProvider<T> extends Provider<List<T>> {

    void bindAdapter(RecyclerView.Adapter<?> adapter);

    int getDataCount();

    T getData(int position);

    boolean isEmpty();

    DataProvider<T> add(T data);

    DataProvider<T> add(int index, T data);

    DataProvider<T> addAll(Collection<T> collection);

    DataProvider<T> addAll(T... data);

    DataProvider<T> addAll(int index, Collection<T> collection);

    DataProvider<T> addDataProvider(DataProvider<T> dataProvider);

    DataProvider<T> remove(T data);

    DataProvider<T> remove(int index);

    DataProvider<T> clear();

    DataProvider<T> swap(int position, int targetPosition);

    DataProvider<T> move(int position, int targetPosition);

    DataProvider<T> each(IntConsumer<T> consumer);
}
