package org.jzl.android.recyclerview.provider;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.EntityWrapperFactory;
import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.android.recyclerview.wrap.EntityWrapper;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.fun.IntConsumer;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WrapDataProvider<T, W extends EntityWrapper<T>> implements DataProvider<T> {

    private DataProvider<W> dataProvider;
    private EntityWrapperFactory<T, W> entityWrapperFactory;

    public WrapDataProvider(EntityWrapperFactory<T, W> entityWrapperFactory) {
        this.dataProvider = ListDataProvider.of();
        this.entityWrapperFactory = ObjectUtils.requireNonNull(entityWrapperFactory, "entityWrapperFactory");
    }

    @Override
    public void bindAdapter(RecyclerView.Adapter<?> adapter) {
        dataProvider.bindAdapter(adapter);
    }

    @Override
    public int getDataCount() {
        return dataProvider.getDataCount();
    }

    @Override
    public T getData(int position) {
        return dataProvider.getData(position).wrap();
    }

    public W getWrapData(int position) {
        return dataProvider.getData(position);
    }

    @Override
    public boolean isEmpty() {
        return dataProvider.isEmpty();
    }

    @Override
    public WrapDataProvider<T, W> add(T data) {
        dataProvider.add(entityWrapperFactory.createEntityWrapper(data));
        return this;
    }

    @Override
    public WrapDataProvider<T, W> add(int index, T data) {
        dataProvider.add(index, entityWrapperFactory.createEntityWrapper(data));
        return this;
    }

    @Override
    public WrapDataProvider<T, W> addAll(Collection<T> collection) {
        dataProvider.addAll(CollectionUtils.map(collection, new ArrayList<>(), entityWrapperFactory::createEntityWrapper));
        return this;
    }

    @SafeVarargs
    @Override
    public final WrapDataProvider<T, W> addAll(T... data) {
        return addAll(Arrays.asList(data));
    }

    @Override
    public WrapDataProvider<T, W> addAll(int index, Collection<T> collection) {
        dataProvider.addAll(index, CollectionUtils.map(collection, new ArrayList<>(), entityWrapperFactory::createEntityWrapper));
        return this;
    }

    @Override
    public WrapDataProvider<T, W> addDataProvider(DataProvider<T> dataProvider) {
        return addAll(dataProvider.provide());
    }

    @Override
    public WrapDataProvider<T, W> remove(T data) {
        for (int i = 0; i < dataProvider.getDataCount(); i++) {
            W w = dataProvider.getData(i);
            if (w.wrap().equals(data)) {
                return remove(i);
            }
        }
        return this;
    }

    @Override
    public WrapDataProvider<T, W> remove(int index) {
        dataProvider.remove(index);
        return this;
    }

    @Override
    public WrapDataProvider<T, W> clear() {
        dataProvider.clear();
        return this;
    }

    @Override
    public WrapDataProvider<T, W> swap(int position, int targetPosition) {
        dataProvider.swap(position, targetPosition);
        return this;
    }

    @Override
    public WrapDataProvider<T, W> move(int position, int targetPosition) {
        dataProvider.move(position, targetPosition);
        return this;
    }

    @Override
    public WrapDataProvider<T, W> each(IntConsumer<T> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        dataProvider.each((index, target) -> consumer.accept(index, target.wrap()));
        return this;
    }

    public WrapDataProvider<T, W> eachWrap(IntConsumer<W> consumer){
        ObjectUtils.requireNonNull(consumer, "consumer");
        dataProvider.each(consumer);
        return this;
    }

    @Override
    public List<T> provide() {
        ArrayList<T> list = new ArrayList<>();
        CollectionUtils.map(dataProvider.provide(), list, EntityWrapper::wrap);
        return Collections.unmodifiableList(list);
    }

}
