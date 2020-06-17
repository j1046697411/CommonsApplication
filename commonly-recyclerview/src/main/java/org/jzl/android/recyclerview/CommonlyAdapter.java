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
import org.jzl.android.recyclerview.fun.ItemViewAttachedToWindow;
import org.jzl.android.recyclerview.fun.ItemViewFactory;
import org.jzl.android.recyclerview.fun.ItemViewHolderListener;
import org.jzl.android.recyclerview.vh.DataBinderCallback;
import org.jzl.android.recyclerview.vh.ViewHolderFactory;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.Collection;
import java.util.List;

public class CommonlyAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public static final int ITEM_TYPE_ALL = -1;
    public static final int ITEM_TYPE_DEFAULT = ITEM_TYPE_ALL;


    private DataProvider<T> dataProvider;
    private ContextProvider contextProvider;
    private ViewHolderFactory<VH> viewHolderFactory;
    private LayoutInflater layoutInflater;
    private DataClassifier<T> dataClassifier;

    private SparseArray<ItemViewFactory> itemViewFactories;
    private SparseArray<List<DataBinder<T, VH>>> dataBinders;
    private SparseArray<List<ItemViewHolderListener<T, VH>>> itemViewHolderListeners;
    private SparseArray<List<ItemViewAttachedToWindow<VH>>> itemViewAttachedToWindows;

    private List<DataProviderBinder<T>> dataProviderBinders;
    private List<DataClassifierBinder<T>> dataClassifierBinders;

    public CommonlyAdapter(
            DataProvider<T> dataProvider,
            ContextProvider contextProvider,
            ViewHolderFactory<VH> viewHolderFactory,
            DataClassifier<T> dataClassifier,
            SparseArray<ItemViewFactory> itemViewFactories,
            SparseArray<List<DataBinder<T, VH>>> dataBinders,
            SparseArray<List<ItemViewHolderListener<T, VH>>> itemViewHolderListeners,
            List<DataProviderBinder<T>> dataProviderBinders,
            List<DataClassifierBinder<T>> dataClassifierBinders,
            SparseArray<List<ItemViewAttachedToWindow<VH>>> itemViewAttachedToWindows
    ) {
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.contextProvider = ObjectUtils.requireNonNull(contextProvider, "contextProvider");
        this.viewHolderFactory = ObjectUtils.requireNonNull(viewHolderFactory, "viewHolderFactory");
        this.layoutInflater = LayoutInflater.from(contextProvider.provide());
        this.dataClassifier = ObjectUtils.requireNonNull(dataClassifier, "dataClassifier");
        this.itemViewFactories = ObjectUtils.requireNonNull(itemViewFactories, "itemViewFactories");
        this.dataBinders = ObjectUtils.requireNonNull(dataBinders, "dataBinders");
        this.itemViewHolderListeners = ObjectUtils.requireNonNull(itemViewHolderListeners, "itemViewHolderListeners");
        this.itemViewAttachedToWindows = ObjectUtils.requireNonNull(itemViewAttachedToWindows, "itemViewAttachedToWindows");

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

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        if (ObjectUtils.nonNull(itemViewAttachedToWindows)) {
            onViewAttachedToWindow(itemViewAttachedToWindows.get(holder.getItemViewType()), holder);
            onViewAttachedToWindow(itemViewAttachedToWindows.get(ITEM_TYPE_ALL), holder);
        }
    }

    private void onViewAttachedToWindow(List<ItemViewAttachedToWindow<VH>> itemViewAttachedToWindows, VH holder) {
        if (CollectionUtils.nonEmpty(itemViewAttachedToWindows)) {
            for (ItemViewAttachedToWindow<VH> itemViewAttachedToWindow : itemViewAttachedToWindows) {
                itemViewAttachedToWindow.onViewAttachedToWindow(holder);
            }
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewFactory itemViewFactory = itemViewFactories.get(viewType);
        if (ObjectUtils.isNull(itemViewFactory)) {
            itemViewFactory = itemViewFactories.get(ITEM_TYPE_ALL);
        }
        ObjectUtils.requireNonNull(itemViewFactory, "itemViewFactory");

        VH holder = viewHolderFactory.createViewHolder(contextProvider, viewType, itemViewFactory.createItemView(layoutInflater, parent));
        onItemViewHolderCreated(holder, viewType);
        return holder;
    }

    protected void onItemViewHolderCreated(VH holder, int viewType) {
        onItemViewHolderCreated(this.itemViewHolderListeners.get(viewType), holder);
        onItemViewHolderCreated(itemViewHolderListeners.get(ITEM_TYPE_ALL), holder);
    }

    private void onItemViewHolderCreated(Collection<ItemViewHolderListener<T, VH>> listeners, VH holder) {
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
        binds(this.dataBinders.get(holder.getItemViewType()), holder, data);
        binds(this.dataBinders.get(ITEM_TYPE_ALL), holder, data);
    }

    private void binds(List<DataBinder<T, VH>> dataBinders, VH holder, T data) {
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
