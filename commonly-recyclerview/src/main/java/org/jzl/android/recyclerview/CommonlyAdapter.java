package org.jzl.android.recyclerview;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.fun.DataBinder;
import org.jzl.android.recyclerview.fun.DataClassifier;
import org.jzl.android.recyclerview.fun.DataClassifierBinder;
import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.android.recyclerview.fun.DataProviderBinder;
import org.jzl.android.recyclerview.fun.ItemViewFactory;
import org.jzl.android.recyclerview.fun.ItemViewHolderListener;
import org.jzl.android.recyclerview.vh.DataBinderCallback;
import org.jzl.android.recyclerview.vh.ViewHolderFactory;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.List;

public class CommonlyAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int ITEM_TYPE_DEFAULT = 0;

    private DataProvider<T> dataProvider;
    private ContextProvider contextProvider;
    private ViewHolderFactory<VH> viewHolderFactory;
    private LayoutInflater layoutInflater;
    private DataClassifier<T> dataClassifier;

    private SparseArray<ItemViewFactory> itemViewFactories;
    private SparseArray<List<DataBinder<T, VH>>> dataBinders;
    private SparseArray<List<ItemViewHolderListener<T, VH>>> itemViewHolderListeners;
    private List<DataProviderBinder<T>> dataProviderBinders;
    private List<DataClassifierBinder<T>> dataClassifierBinders;

    public CommonlyAdapter(DataProvider<T> dataProvider, ContextProvider contextProvider, ViewHolderFactory<VH> viewHolderFactory, DataClassifier<T> dataClassifier, SparseArray<ItemViewFactory> itemViewFactories, SparseArray<List<DataBinder<T, VH>>> dataBinders, SparseArray<List<ItemViewHolderListener<T, VH>>> itemViewHolderListeners, List<DataProviderBinder<T>> dataProviderBinders, List<DataClassifierBinder<T>> dataClassifierBinders) {
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.contextProvider = ObjectUtils.requireNonNull(contextProvider, "contextProvider");
        this.viewHolderFactory = ObjectUtils.requireNonNull(viewHolderFactory, "viewHolderFactory");
        this.layoutInflater = LayoutInflater.from(contextProvider.provide());
        this.dataClassifier = ObjectUtils.requireNonNull(dataClassifier, "dataClassifier");
        this.itemViewFactories = ObjectUtils.requireNonNull(itemViewFactories, "itemViewFactories");
        this.dataBinders = ObjectUtils.requireNonNull(dataBinders, "dataBinders");
        this.itemViewHolderListeners = ObjectUtils.requireNonNull(itemViewHolderListeners, "itemViewHolderListeners");

        this.dataProviderBinders = dataProviderBinders;
        this.dataClassifierBinders = dataClassifierBinders;

        this.dataProvider.bindAdapter(this);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (CollectionUtils.nonEmpty(dataProviderBinders)) {
            for (DataProviderBinder<T> binder : dataProviderBinders) {
                binder.bind(dataProvider);
            }
        }
        if (CollectionUtils.nonEmpty(dataClassifierBinders)) {
            for (DataClassifierBinder<T> dataClassifierBinder : dataClassifierBinders) {
                dataClassifierBinder.bind(dataClassifier);
            }
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewFactory itemViewFactory = itemViewFactories.get(viewType);
        if (ObjectUtils.isNull(itemViewFactory)) {
            throw new NullPointerException(String.format("itemViewFactory is null, viewType is %d", viewType));
        }
        VH holder = viewHolderFactory.createViewHolder(contextProvider, viewType, itemViewFactory.createItemView(layoutInflater, parent));
        onItemViewHolderCreated(holder, viewType);
        return holder;
    }

    protected void onItemViewHolderCreated(VH holder, int viewType) {
        List<ItemViewHolderListener<T, VH>> listeners = itemViewHolderListeners.get(viewType);
        if (CollectionUtils.nonEmpty(listeners)) {
            for (ItemViewHolderListener<T, VH> listener : listeners) {
                listener.onItemViewHolderCreated(holder);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        T data = dataProvider.getData(position);
        if (holder instanceof DataBinderCallback) {
            ((DataBinderCallback) holder).beforeBindViewHolder();
            bind(holder, data);
            ((DataBinderCallback) holder).afterBindViewHolder();
        } else {
            bind(holder, data);
        }
    }

    private void bind(VH holder, T data) {
        List<DataBinder<T, VH>> dataBinders = this.dataBinders.get(holder.getItemViewType());
        if (CollectionUtils.nonEmpty(dataBinders)) {
            for (DataBinder<T, VH> binder : dataBinders) {
                binder.bind(holder, data);
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataProvider.getDataCount();
    }

    @Override
    public int getItemViewType(int position) {
        return dataClassifier.getItemType(position, dataProvider.getData(position));
    }

}
