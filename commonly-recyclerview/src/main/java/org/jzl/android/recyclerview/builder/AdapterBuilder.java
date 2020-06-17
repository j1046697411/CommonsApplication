package org.jzl.android.recyclerview.builder;

import android.util.SparseArray;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;
import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.EntityWrapperFactory;
import org.jzl.android.recyclerview.fun.DataBinder;
import org.jzl.android.recyclerview.fun.DataClassifier;
import org.jzl.android.recyclerview.fun.DataClassifierBinder;
import org.jzl.android.recyclerview.fun.DataProviderBinder;
import org.jzl.android.recyclerview.fun.ItemViewAttachedToWindow;
import org.jzl.android.recyclerview.fun.ItemViewFactory;
import org.jzl.android.recyclerview.fun.ItemViewHolderListener;
import org.jzl.android.recyclerview.wrap.EntityWrapper;

import java.util.Collection;
import java.util.List;

public interface AdapterBuilder<T, VH extends RecyclerView.ViewHolder> {

    AdapterBuilder<T, VH> itemViews(ItemViewFactory itemViewFactory, int... viewTypes);

    AdapterBuilder<T, VH> dataBinds(DataBinder<T, VH> dataBinder, int... viewTypes);

    AdapterBuilder<T, VH> itemTypes(DataClassifier<T> dataClassifier);

    AdapterBuilder<T, VH> viewHolders(ItemViewHolderListener<T, VH> listener, int... viewTypes);

    AdapterBuilder<T, VH> itemViewAttachedToWindows(ItemViewAttachedToWindow<VH> itemViewAttachedToWindow, int... viewTypes);

    AdapterBuilder<T, VH> bindDataProviderBinder(DataProviderBinder<T> binder);

    AdapterBuilder<T, VH> bindDataClassifierBinder(DataClassifierBinder<T> binder);

    AdapterBuilder<T, VH> data(T... data);

    AdapterBuilder<T, VH> data(Collection<T> data);

    boolean isWrap();

    <W extends EntityWrapper<T>> AdapterBuilder<T, VH> wrap(EntityWrapperFactory<T, W> factory);

    CommonlyAdapter<T, VH> build(ContextProvider contextProvider);

}
