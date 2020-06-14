package org.jzl.android.recyclerview.plugins;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.refresh.DataLoader;
import org.jzl.android.recyclerview.refresh.RefreshLayout;
import org.jzl.android.recyclerview.refresh.RefreshLoadMoreHelper;
import org.jzl.lang.util.ObjectUtils;

public class RefreshLoadMorePlugin<R, T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

    private RefreshLayout refreshLayout;
    private R request;
    private DataLoader<R, T> dataLoader;
    private RefreshLoadMoreHelper.Callback<R> callback;

    private RefreshLoadMorePlugin(RefreshLayout refreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
        this.refreshLayout = ObjectUtils.requireNonNull(refreshLayout, "refreshLayout");
        this.request = ObjectUtils.requireNonNull(request, "request");
        this.dataLoader = ObjectUtils.requireNonNull(dataLoader, "dataLoader");
        this.callback = ObjectUtils.requireNonNull(callback, "callback");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        configurator.bindDataProviderBinder(dataProvider -> RefreshLoadMoreHelper.of(refreshLayout, dataProvider, dataLoader, callback, request));
    }

    public static <R, T, VH extends RecyclerView.ViewHolder> RefreshLoadMorePlugin<R, T, VH> of(RefreshLayout refreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
        return new RefreshLoadMorePlugin<>(refreshLayout, request, dataLoader, callback);
    }

    public static <R extends PageRequest<R>, T, VH extends RecyclerView.ViewHolder> RefreshLoadMorePlugin<R, T, VH> of(RefreshLayout refreshLayout, R request, DataLoader<R, T> dataLoader) {
        return of(refreshLayout, request, dataLoader, (isRefresh, req) -> {
            if (isRefresh) {
                return req.firstPage();
            } else {
                return req.nextPage();
            }
        });
    }

    public interface PageRequest<R extends PageRequest<R>> {

        R nextPage();

        R firstPage();
    }

}
