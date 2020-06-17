package org.jzl.android.recyclerview.plugins;

import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.refresh.DataLoader;
import org.jzl.android.recyclerview.refresh.RefreshLayout;
import org.jzl.android.recyclerview.refresh.RefreshLoadMoreHelper;
import org.jzl.lang.util.ObjectUtils;

import java.util.concurrent.ExecutorService;

public class RefreshLoadMorePlugin<R, T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

    private RefreshLayout refreshLayout;
    private R request;
    private DataLoader<R, T> dataLoader;
    private RefreshLoadMoreHelper.Callback<R> callback;
    private ExecutorService executorService;
    private Handler mainHandler;

    private RefreshLoadMorePlugin(ExecutorService executorService, Handler mainHandler, RefreshLayout refreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
        this.executorService = ObjectUtils.requireNonNull(executorService, "executorService");
        this.mainHandler = ObjectUtils.requireNonNull(mainHandler, "mainHandler");
        this.refreshLayout = ObjectUtils.requireNonNull(refreshLayout, "refreshLayout");
        this.request = ObjectUtils.requireNonNull(request, "request");
        this.dataLoader = ObjectUtils.requireNonNull(dataLoader, "dataLoader");
        this.callback = ObjectUtils.requireNonNull(callback, "callback");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        configurator.bindDataProviderBinder(dataProvider -> RefreshLoadMoreHelper.of(executorService, mainHandler, refreshLayout, dataProvider, dataLoader, callback, request));
    }

    public static <R, T, VH extends RecyclerView.ViewHolder> RefreshLoadMorePlugin<R, T, VH> of(ExecutorService executorService, Handler mainHandler,RefreshLayout refreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
        return new RefreshLoadMorePlugin<>(executorService, mainHandler, refreshLayout, request, dataLoader, callback);
    }

    public static <R extends PageRequest<R>, T, VH extends RecyclerView.ViewHolder> RefreshLoadMorePlugin<R, T, VH> of(ExecutorService executorService, Handler mainHandler,RefreshLayout refreshLayout, R request, DataLoader<R, T> dataLoader) {
        return of(executorService, mainHandler, refreshLayout, request, dataLoader, (isRefresh, req) -> {
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
