package org.jzl.android.recyclerview.builder;

import android.util.SparseArray;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.EntityWrapperFactory;
import org.jzl.android.recyclerview.fun.DataBinder;
import org.jzl.android.recyclerview.fun.DataClassifier;
import org.jzl.android.recyclerview.fun.DataClassifierBinder;
import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.android.recyclerview.fun.DataProviderBinder;
import org.jzl.android.recyclerview.fun.ItemViewAttachedToWindow;
import org.jzl.android.recyclerview.fun.ItemViewFactory;
import org.jzl.android.recyclerview.fun.ItemViewHolderListener;
import org.jzl.android.recyclerview.provider.ListDataProvider;
import org.jzl.android.recyclerview.provider.WrapDataProvider;
import org.jzl.android.recyclerview.vh.CommonlyViewHolder;
import org.jzl.android.recyclerview.vh.ViewHolderFactory;
import org.jzl.android.recyclerview.wrap.EntityWrapper;
import org.jzl.android.util.SparseArrayUtils;
import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommonlyAdapterBuilder<T, VH extends RecyclerView.ViewHolder> implements AdapterBuilder<T, VH> {

    private ViewHolderFactory<VH> viewHolderFactory;
    private DataClassifier<T> dataClassifier;
    private DataProvider<T> dataProvider;

    private SparseArray<ItemViewFactory> itemViewFactories = new SparseArray<>();
    private SparseArray<List<DataBinder<T, VH>>> dataBinders = new SparseArray<>();
    private SparseArray<List<ItemViewHolderListener<T, VH>>> listeners = new SparseArray<>();
    private List<DataProviderBinder<T>> dataProviderBinders = new ArrayList<>();
    private List<DataClassifierBinder<T>> dataClassifierBinders = new ArrayList<>();
    private SparseArray<List<ItemViewAttachedToWindow<VH>>> itemViewAttachedToWindows = new SparseArray<>();

    private EntityWrapperFactory<T, ? extends EntityWrapper<T>> entityWrapperFactory;

    public CommonlyAdapterBuilder(ViewHolderFactory<VH> viewHolderFactory, DataProvider<T> dataProvider) {
        this.dataClassifier = (position, data) -> CommonlyAdapter.ITEM_TYPE_DEFAULT;
        this.viewHolderFactory = ObjectUtils.requireNonNull(viewHolderFactory, "viewHolderFactory");
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
    }

    @Override
    public AdapterBuilder<T, VH> itemViews(ItemViewFactory itemViewFactory, int... viewTypes) {
        if (ObjectUtils.isNull(itemViewFactory)) {
            return this;
        }
        return puts(itemViewFactories, itemViewFactory, viewTypes);
    }

    @Override
    public AdapterBuilder<T, VH> dataBinds(DataBinder<T, VH> dataBinder, int... viewTypes) {
        if (ObjectUtils.isNull(dataBinder)) {
            return this;
        }
        return putLists(dataBinders, dataBinder, viewTypes);
    }

    @Override
    public AdapterBuilder<T, VH> itemTypes(DataClassifier<T> dataClassifier) {
        this.dataClassifier = ObjectUtils.get(dataClassifier, this.dataClassifier);
        return this;
    }

    @Override
    public AdapterBuilder<T, VH> viewHolders(ItemViewHolderListener<T, VH> listener, int... viewTypes) {
        if (ObjectUtils.isNull(listener)) {
            return this;
        }
        return putLists(listeners, listener, viewTypes);
    }

    @Override
    public AdapterBuilder<T, VH> itemViewAttachedToWindows(ItemViewAttachedToWindow<VH> itemViewAttachedToWindow, int... viewTypes) {
        return putLists(this.itemViewAttachedToWindows, itemViewAttachedToWindow, viewTypes);
    }

    @Override
    public AdapterBuilder<T, VH> bindDataProviderBinder(DataProviderBinder<T> binder) {
        if (ObjectUtils.nonNull(binder)) {
            this.dataProviderBinders.add(binder);
        }
        return this;
    }

    @Override
    public AdapterBuilder<T, VH> bindDataClassifierBinder(DataClassifierBinder<T> binder) {
        if (ObjectUtils.nonNull(binder)) {
            this.dataClassifierBinders.add(binder);
        }
        return this;
    }

    @SafeVarargs
    @Override
    public final AdapterBuilder<T, VH> data(T... data) {
        dataProvider.addAll(data);
        return this;
    }

    @Override
    public AdapterBuilder<T, VH> data(Collection<T> data) {
        dataProvider.addAll(data);
        return this;
    }

    @Override
    public <W extends EntityWrapper<T>> AdapterBuilder<T, VH> wrap(EntityWrapperFactory<T, W> factory) {
        this.entityWrapperFactory = factory;
        return this;
    }

    public boolean isWrap() {
        return entityWrapperFactory != null;
    }

    @Override
    public CommonlyAdapter<T, VH> build(ContextProvider contextProvider) {
        ObjectUtils.requireNonNull(contextProvider, "contextProvider");
        DataProvider<T> dataProvider;
        if (ObjectUtils.nonNull(entityWrapperFactory)) {
            dataProvider = new WrapDataProvider<>(entityWrapperFactory);
            dataProvider.addDataProvider(this.dataProvider);
        } else {
            dataProvider = this.dataProvider;
        }
        return new CommonlyAdapter<>(
                dataProvider,
                contextProvider,
                viewHolderFactory,
                dataClassifier,
                itemViewFactories,
                dataBinders,
                listeners,
                dataProviderBinders,
                dataClassifierBinders,
                itemViewAttachedToWindows
        );
    }

    private <V> CommonlyAdapterBuilder<T, VH> puts(SparseArray<V> sparseArray, V value, int... viewTypes) {
        if (ArrayUtils.isEmpty(viewTypes)) {
            if (sparseArray.indexOfKey(CommonlyAdapter.ITEM_TYPE_DEFAULT) == -1) {
                sparseArray.put(CommonlyAdapter.ITEM_TYPE_DEFAULT, value);
            }
        } else {
            SparseArrayUtils.puts(sparseArray, value, viewTypes);
        }
        return this;
    }

    private <V> CommonlyAdapterBuilder<T, VH> putLists(SparseArray<List<V>> sparseArray, V value, int... viewTypes) {
        if (ArrayUtils.isEmpty(viewTypes)) {
            putLists(sparseArray, sparseArray.get(CommonlyAdapter.ITEM_TYPE_DEFAULT), CommonlyAdapter.ITEM_TYPE_DEFAULT, value);
        } else {
            for (int viewType : viewTypes) {
                putLists(sparseArray, sparseArray.get(viewType), viewType, value);
            }
        }
        return this;
    }

    private <V> void putLists(SparseArray<List<V>> sparseArray, List<V> list, int key, V value) {
        if (ObjectUtils.nonNull(list)) {
            list.add(value);
        } else {
            list = new ArrayList<>();
            list.add(value);
            sparseArray.put(key, list);
        }
    }

    public static <T, VH extends RecyclerView.ViewHolder, DP extends DataProvider<T>> CommonlyAdapterBuilder<T, VH> of(ViewHolderFactory<VH> viewHolderFactory, DP dataProvider) {
        return new CommonlyAdapterBuilder<>(viewHolderFactory, dataProvider);
    }

    @SafeVarargs
    public static <T> CommonlyAdapterBuilder<T, CommonlyViewHolder> of(T... data) {
        return of((provider, viewType, itemView) -> new CommonlyViewHolder(itemView), ListDataProvider.of(data));
    }
}
