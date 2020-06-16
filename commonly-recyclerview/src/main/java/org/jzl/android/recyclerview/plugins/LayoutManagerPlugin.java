package org.jzl.android.recyclerview.plugins;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.lang.util.ObjectUtils;

public class LayoutManagerPlugin<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

    private RecyclerViewConfigurator.LayoutManagerFactory factory;

    private LayoutManagerPlugin(RecyclerViewConfigurator.LayoutManagerFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        configurator.layoutManager(factory);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> linearLayoutManager(int orientation, boolean reverseLayout) {
        return new LayoutManagerPlugin<>(contextProvider -> new LinearLayoutManager(contextProvider.provide(), orientation, reverseLayout));
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> linearLayoutManager(int orientation) {
        return linearLayoutManager(orientation, false);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> linearLayoutManager() {
        return linearLayoutManager(LinearLayoutManager.VERTICAL);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> gridLayoutManager(int spanCount, int orientation, boolean reverseLayout, LayoutManagerPlugin.SpanSizeLookup spanSizeLookup) {
        return new LayoutManagerPlugin<>(contextProvider -> {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(contextProvider.provide(), spanCount, orientation, reverseLayout);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (ObjectUtils.nonNull(spanSizeLookup)) {
                        return spanSizeLookup.getSpanSize(position);
                    } else {
                        return 1;
                    }
                }
            });
            return gridLayoutManager;
        });
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> gridLayoutManager(int spanCount, int orientation, LayoutManagerPlugin.SpanSizeLookup spanSizeLookup) {
        return gridLayoutManager(spanCount, orientation, false, spanSizeLookup);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> gridLayoutManager(int spanCount, int orientation) {
        return gridLayoutManager(spanCount, orientation, null);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> gridLayoutManager(int spanCount, LayoutManagerPlugin.SpanSizeLookup spanSizeLookup) {
        return gridLayoutManager(spanCount, GridLayoutManager.VERTICAL, spanSizeLookup);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> gridLayoutManager(int spanCount) {
        return gridLayoutManager(spanCount, null);
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> staggeredGridLayoutManager(int spanCount, int orientation) {
        return new LayoutManagerPlugin<>(contextProvider -> new StaggeredGridLayoutManager(spanCount, orientation));
    }

    public static <T, VH extends RecyclerView.ViewHolder> LayoutManagerPlugin<T, VH> staggeredGridLayoutManager(int spanCount) {
        return staggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
    }

    public interface SpanSizeLookup {
        int getSpanSize(int position);
    }
}
