package org.jzl.android.recyclerview;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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
    public static final int ITEM_TYPE_EMPTY = -2;

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
    private EmptyLayout<?, VH> emptyLayout;

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
        if (isEnableEmptyLayout()) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        Log.d("test", position + "|" + gridLayoutManager.getSpanCount());
                        if (isEnableEmptyLayout()) {
                            return gridLayoutManager.getSpanCount();
                        } else if (ObjectUtils.nonNull(spanSizeLookup)) {
                            return spanSizeLookup.getSpanSize(position);
                        } else {
                            return 1;
                        }
                    }
                });
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
        ItemViewFactory itemViewFactory;
        if (isEmptyLayout(viewType)) {
            itemViewFactory = emptyLayout;
        } else {
            itemViewFactory = itemViewFactories.get(viewType);
            if (ObjectUtils.isNull(itemViewFactory)) {
                itemViewFactory = itemViewFactories.get(ITEM_TYPE_ALL);
            }
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
        if (isEmptyLayout(holder.getItemViewType())) {
            if (holder instanceof DataBinderCallback) {
                ((DataBinderCallback) holder).beforeBindViewHolder();
                emptyLayout.bind(holder);
                ((DataBinderCallback) holder).afterBindViewHolder();
            } else {
                emptyLayout.bind(holder);
            }
        } else {
            T data = dataProvider.getData(position);
            if (holder instanceof DataBinderCallback) {
                ((DataBinderCallback) holder).beforeBindViewHolder();
                bind(holder, data);
                ((DataBinderCallback) holder).afterBindViewHolder();
            } else {
                bind(holder, data);
            }
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
        if (isEnableEmptyLayout()) {
            return 1;
        } else {
            return dataProvider.getDataCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isEnableEmptyLayout()) {
            return ITEM_TYPE_EMPTY;
        }
        return dataClassifier.getItemType(position, dataProvider.getData(position));
    }

    public CommonlyAdapter<T, VH> setEmptyLayout(EmptyLayout<?, VH> emptyLayout) {
        this.emptyLayout = emptyLayout;
        return this;
    }

    protected boolean isEnableEmptyLayout() {
        return dataProvider.isEmpty() && ObjectUtils.nonNull(emptyLayout);
    }

    protected boolean isEmptyLayout(int viewType) {
        return isEnableEmptyLayout() && viewType == ITEM_TYPE_EMPTY;
    }

    public interface EmptyLayout<T, VH extends RecyclerView.ViewHolder> extends ItemViewFactory, DataBinder<T, VH> {

        T getEmptyData();

        @Override
        View createItemView(LayoutInflater layoutInflater, ViewGroup parent);

        @Override
        void bind(VH holder, T data);

        default void bind(VH holder) {
            bind(holder, getEmptyData());
        }

    }

}
