package org.jzl.android.recyclerview.builder;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import org.jzl.lang.util.ObjectUtils;

import java.util.Collection;

public interface RecyclerViewConfigurator<T, VH extends RecyclerView.ViewHolder> {

    RecyclerViewConfigurator<T, VH> layoutManager(LayoutManagerFactory layoutManagerFactory);

    RecyclerViewConfigurator<T, VH> itemTouchHelper(ItemTouchHelper touchHelper);

    RecyclerViewConfigurator<T, VH> bind(Binder... binders);

    RecyclerViewConfigurator<T, VH> dataBinds(DataBinder<T, VH> dataBinder, int... viewTypes);

    RecyclerViewConfigurator<T, VH> itemViewAttachedToWindows(ItemViewAttachedToWindow<VH> itemViewAttachedToWindow, int... viewTypes);

    RecyclerViewConfigurator<T, VH> bindDataProviderBinder(DataProviderBinder<T> binder);

    RecyclerViewConfigurator<T, VH> bindDataClassifierBinder(DataClassifierBinder<T> binder);

    RecyclerViewConfigurator<T, VH> data(T... data);

    RecyclerViewConfigurator<T, VH> data(Collection<T> data);

    RecyclerViewConfigurator<T, VH> itemTypes(DataClassifier<T> dataClassifier);

    RecyclerViewConfigurator<T, VH> itemViews(ItemViewFactory itemViewFactory, int... viewTypes);

    <W extends EntityWrapper<T>> RecyclerViewConfigurator<T, VH> wrap(EntityWrapperFactory<T, W> factory);

    boolean isWrap();

    RecyclerViewConfigurator<T, VH> emptyLayout(CommonlyAdapter.EmptyLayout<?, VH> emptyLayout);

    default RecyclerViewConfigurator<T, VH> itemViews(final  @LayoutRes int layoutId,final boolean attachToRoot, int... viewTypes) {
        return itemViews((layoutInflater, parent) -> layoutInflater.inflate(layoutId, parent, attachToRoot), viewTypes);
    }

    default RecyclerViewConfigurator<T, VH> itemViews(@LayoutRes int layoutId, int... viewTypes) {
        return itemViews(layoutId, false, viewTypes);
    }

    RecyclerViewConfigurator<T, VH> viewHolders(ItemViewHolderListener<T, VH> listener, int... viewTypes);

    RecyclerViewConfigurator<T, VH> plugin(RecyclerViewPlugin<T, VH> plugin, int... viewTypes);

    default void bind(RecyclerView recyclerView) {
        ObjectUtils.requireNonNull(recyclerView, "recyclerView");
        bind(ContextProvider.of(recyclerView), recyclerView);
    }

    void bind(ContextProvider contextProvider, RecyclerView recyclerView);

    interface LayoutManagerFactory {
        RecyclerView.LayoutManager createLayoutManager(ContextProvider contextProvider);
    }

    interface Binder {
        void bind(ContextProvider contextProvider, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager);
    }

    interface RecyclerViewPlugin<T, VH extends RecyclerView.ViewHolder> {

        void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes);
    }

}
