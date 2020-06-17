package org.jzl.android.recyclerview.builder;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import org.jzl.android.recyclerview.vh.CommonlyViewHolder;
import org.jzl.android.recyclerview.vh.ViewHolderFactory;
import org.jzl.android.recyclerview.wrap.EntityWrapper;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommonlyRecyclerViewConfigurator<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator<T, VH> {

    private AdapterBuilder<T, VH> adapterBuilder;
    private ItemTouchHelper itemTouchHelper;
    private List<Binder> binders = new ArrayList<>();
    private LayoutManagerFactory layoutManagerFactory = contextProvider -> new LinearLayoutManager(contextProvider.provide(), RecyclerView.VERTICAL, false);
    private List<RecyclerViewPluginHolder> holders = new ArrayList<>();

    public CommonlyRecyclerViewConfigurator(AdapterBuilder<T, VH> adapterBuilder) {
        this.adapterBuilder = ObjectUtils.requireNonNull(adapterBuilder, "adapterBuilder");
    }

    @Override
    public RecyclerViewConfigurator<T, VH> layoutManager(LayoutManagerFactory layoutManagerFactory) {
        this.layoutManagerFactory = ObjectUtils.get(layoutManagerFactory, this.layoutManagerFactory);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> itemTouchHelper(ItemTouchHelper touchHelper) {
        this.itemTouchHelper = touchHelper;
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> bind(Binder... binders) {
        CollectionUtils.addAll(this.binders, binders);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> dataBinds(DataBinder<T, VH> dataBinder, int... viewTypes) {
        adapterBuilder.dataBinds(dataBinder, viewTypes);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> itemViewAttachedToWindows(ItemViewAttachedToWindow<VH> itemViewAttachedToWindow, int... viewTypes) {
        adapterBuilder.itemViewAttachedToWindows(itemViewAttachedToWindow, viewTypes);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> bindDataProviderBinder(DataProviderBinder<T> binder) {
        adapterBuilder.bindDataProviderBinder(binder);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> bindDataClassifierBinder(DataClassifierBinder<T> binder) {
        adapterBuilder.bindDataClassifierBinder(binder);
        return this;
    }

    @SafeVarargs
    @Override
    public final RecyclerViewConfigurator<T, VH> data(T... data) {
        adapterBuilder.data(data);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> data(Collection<T> data) {
        adapterBuilder.data(data);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> itemTypes(DataClassifier<T> dataClassifier) {
        adapterBuilder.itemTypes(dataClassifier);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> itemViews(ItemViewFactory itemViewFactory, int... viewTypes) {
        adapterBuilder.itemViews(itemViewFactory, viewTypes);
        return this;
    }

    @Override
    public <W extends EntityWrapper<T>> RecyclerViewConfigurator<T, VH> wrap(EntityWrapperFactory<T, W> factory) {
        adapterBuilder.wrap(factory);
        return this;
    }

    @Override
    public boolean isWrap() {
        return adapterBuilder.isWrap();
    }

    @Override
    public RecyclerViewConfigurator<T, VH> viewHolders(ItemViewHolderListener<T, VH> listener, int... viewTypes) {
        adapterBuilder.viewHolders(listener, viewTypes);
        return this;
    }

    @Override
    public RecyclerViewConfigurator<T, VH> plugin(RecyclerViewPlugin<T, VH> plugin, int... viewTypes) {
        holders.add(new RecyclerViewPluginHolder(plugin, viewTypes));
        return this;
    }

    @Override
    public void bind(ContextProvider contextProvider, RecyclerView recyclerView) {
        ObjectUtils.requireNonNull(recyclerView, "recyclerView");
        ObjectUtils.requireNonNull(contextProvider, "contextProvider");
        plugins(1);
        CommonlyAdapter<T, VH> adapter = adapterBuilder.build(contextProvider);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = layoutManagerFactory.createLayoutManager(contextProvider);
        recyclerView.setLayoutManager(layoutManager);
        if (ObjectUtils.nonNull(itemTouchHelper)) {
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        binds(contextProvider, recyclerView, layoutManager);
    }

    private void plugins(int nesting) {
        List<RecyclerViewPluginHolder> holders = new ArrayList<>(this.holders);
        this.holders.clear();
        for (RecyclerViewPluginHolder holder : holders) {
            holder.plugin.setup(this, holder.viewTypes);
        }
        if (CollectionUtils.nonEmpty(this.holders) && nesting <= 10) {
            plugins(nesting + 1);
        }
    }

    private void binds(ContextProvider contextProvider, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {

        if (CollectionUtils.nonEmpty(this.binders)) {
            for (Binder binder : this.binders) {
                binder.bind(contextProvider, recyclerView, layoutManager);
            }
        }
    }

    @SafeVarargs
    public static <T> CommonlyRecyclerViewConfigurator<T, CommonlyViewHolder> of(T... data) {
        return of((provider, viewType, itemView) -> new CommonlyViewHolder(itemView), ListDataProvider.of(data));
    }

    public static <T, VH extends RecyclerView.ViewHolder, DP extends DataProvider<T>> CommonlyRecyclerViewConfigurator<T, VH> of(ViewHolderFactory<VH> viewHolderFactory, DP dataProvider) {
        return of(CommonlyAdapterBuilder.of(viewHolderFactory, dataProvider));
    }

    public static <T, VH extends RecyclerView.ViewHolder, DP extends DataProvider<T>> CommonlyRecyclerViewConfigurator<T, VH> of(AdapterBuilder<T, VH> adapterBuilder) {
        return new CommonlyRecyclerViewConfigurator<>(adapterBuilder);
    }

    private class RecyclerViewPluginHolder {

        private RecyclerViewPlugin<T, VH> plugin;
        private int[] viewTypes;

        public RecyclerViewPluginHolder(RecyclerViewPlugin<T, VH> plugin, int... viewTypes) {
            this.plugin = plugin;
            this.viewTypes = viewTypes;
        }

        public RecyclerViewPlugin<T, VH> getPlugin() {
            return plugin;
        }

        public int[] getViewTypes() {
            return viewTypes;
        }
    }
}
