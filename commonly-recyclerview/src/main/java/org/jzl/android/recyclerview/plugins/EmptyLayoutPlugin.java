package org.jzl.android.recyclerview.plugins;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.CommonlyAdapter;
import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.fun.DataBinder;
import org.jzl.android.recyclerview.fun.ItemViewFactory;
import org.jzl.lang.util.ObjectUtils;

public class EmptyLayoutPlugin<E, T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

    private E emptyData;
    private ItemViewFactory emptyItemViewFactory;
    private DataBinder<E, VH> dataBinder;

    private EmptyLayoutPlugin(E emptyData, ItemViewFactory emptyItemViewFactory, DataBinder<E, VH> dataBinder) {
        this.emptyData = emptyData;
        this.emptyItemViewFactory = ObjectUtils.requireNonNull(emptyItemViewFactory, "emptyItemViewFactory");
        this.dataBinder = dataBinder;
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        configurator.emptyLayout(new CommonlyAdapter.EmptyLayout<E, VH>() {
            @Override
            public E getEmptyData() {
                return emptyData;
            }

            @Override
            public View createItemView(LayoutInflater layoutInflater, ViewGroup parent) {
                return emptyItemViewFactory.createItemView(layoutInflater, parent);
            }

            @Override
            public void bind(VH holder, E data) {
                if (ObjectUtils.nonNull(data) && ObjectUtils.nonNull(dataBinder)) {
                    dataBinder.bind(holder, data);
                }
            }
        });
    }

    public static <E, T, VH extends RecyclerView.ViewHolder> EmptyLayoutPlugin<E, T, VH> of(E emptyData, ItemViewFactory emptyItemViewFactory, DataBinder<E, VH> dataBinder) {
        return new EmptyLayoutPlugin<>(emptyData, emptyItemViewFactory, dataBinder);
    }

    public static <E, T, VH extends RecyclerView.ViewHolder> EmptyLayoutPlugin<E, T, VH> of(@LayoutRes int layoutId, boolean attachToRoot) {
        return of(null, (layoutInflater, parent) -> layoutInflater.inflate(layoutId, parent, attachToRoot), null);
    }

    public static <E, T, VH extends RecyclerView.ViewHolder> EmptyLayoutPlugin<E, T, VH> of(@LayoutRes int layoutId) {
        return of(layoutId, false);
    }

}
