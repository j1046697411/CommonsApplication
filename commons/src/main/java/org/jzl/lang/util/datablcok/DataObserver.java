package org.jzl.lang.util.datablcok;

public interface DataObserver {

    void onInserted(int position, int count);

    void onRemoved(int position, int count);

    void onMoved(int fromPosition, int toPosition);

    void onChanged(int position);

    void onBeforeAllChanged();

    void onAllChanged();
}
