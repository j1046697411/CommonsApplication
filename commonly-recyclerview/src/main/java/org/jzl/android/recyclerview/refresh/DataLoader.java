package org.jzl.android.recyclerview.refresh;

import java.util.Collection;

public interface DataLoader<R, T> {

    void load(R request, boolean isRefresh, Callback<T> callback);

    interface Callback<T> {

        void finishedLoad(boolean isRefresh, int delay, boolean success, boolean noMoreData, Collection<T> data);

    }
}
