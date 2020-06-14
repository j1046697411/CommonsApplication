package org.jzl.android.recyclerview.refresh;

public interface RefreshLayout {

    void finishRefresh(int delay, boolean success, boolean noMoreData);

    void finishLoadMore(int delay, boolean success, boolean noMoreData);

    void setRefreshListener(OnRefreshListener refreshListener);

    void setLoadMoreListener(OnLoadMoreListener loadMoreListener);
}
