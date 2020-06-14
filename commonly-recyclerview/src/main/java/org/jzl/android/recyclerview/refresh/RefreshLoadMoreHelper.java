package org.jzl.android.recyclerview.refresh;

import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.Collection;

public class RefreshLoadMoreHelper<R, T> implements DataLoader.Callback<T>, OnRefreshLoadMoreListener {

    private DataProvider<T> dataProvider;
    private DataLoader<R, T> dataLoader;
    private RefreshLayout refreshLayout;
    private Callback<R> callback;
    private R request;

    private RefreshLoadMoreHelper(RefreshLayout refreshLayout, DataProvider<T> dataProvider, DataLoader<R, T> dataLoader, Callback<R> callback, R request) {
        this.refreshLayout = ObjectUtils.requireNonNull(refreshLayout, "refreshLayout");
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.dataLoader = ObjectUtils.requireNonNull(dataLoader, "dataLoader");
        this.callback = ObjectUtils.requireNonNull(callback, "callback");
        this.request = request;

        refreshLayout.setLoadMoreListener(this);
        refreshLayout.setRefreshListener(this);
    }

    @Override
    public void finishedLoad(boolean isRefresh, int delay, boolean success, boolean noMoreData, Collection<T> data) {
        if (CollectionUtils.nonEmpty(data)) {
            if (isRefresh) {
                dataProvider.clear();
            }
            loadFinished(data);
        }
        if (isRefresh) {
            finishRefresh(delay, success, noMoreData);
        } else {
            finishLoadMore(delay, success, noMoreData);
        }
    }

    private void loadFinished(Collection<T> data) {
        dataProvider.addAll(data);
    }

    private void finishRefresh(int delay, boolean success, boolean noMoreData) {
        refreshLayout.finishRefresh(delay, success, noMoreData);
    }

    private void finishLoadMore(int delay, boolean success, boolean noMoreData) {
        refreshLayout.finishLoadMore(delay, success, noMoreData);
    }

    public static <R, T> RefreshLoadMoreHelper<R, T> of(RefreshLayout refreshLayout, DataProvider<T> dataProvider, DataLoader<R, T> dataLoader, Callback<R> callback, R request) {
        return new RefreshLoadMoreHelper<>(refreshLayout, dataProvider, dataLoader, callback, request);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        this.dataLoader.load(callback.updateRequest(false, request), false, this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        this.dataLoader.load(callback.updateRequest(true, request), true, this);
    }

    public interface Callback<R> {
        R updateRequest(boolean isRefresh, R request);
    }
}
