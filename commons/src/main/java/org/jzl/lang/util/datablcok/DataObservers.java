package org.jzl.lang.util.datablcok;

import org.jzl.lang.util.ForeachUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

class DataObservers implements DataObserver, DirtyAble {

    private final Set<DataObserver> dataObservers = new HashSet<>();
    private final Set<DirtyAble> dirtyAbles = new HashSet<>();
    private ThreadLocal<AtomicInteger> semaphore = new ThreadLocal<>();

    public DataObservers() {
        semaphore.set(new AtomicInteger(0));
    }

    @Override
    public void onInserted(int position, int count) {
        dirty();
        if (isEnable()) {
            ForeachUtils.each(this.dataObservers, target -> target.onInserted(position, count));
        }
    }

    @Override
    public void onRemoved(int position, int count) {
        dirty();
        if (isEnable()) {
            ForeachUtils.each(this.dataObservers, target -> target.onRemoved(position, count));
        }
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        dirty();
        if (isEnable()) {
            ForeachUtils.each(this.dataObservers, target -> target.onMoved(fromPosition, toPosition));
        }
    }

    @Override
    public void onChanged(int position) {
        dirty();
        if (isEnable()) {
            ForeachUtils.each(dataObservers, target -> target.onChanged(position));
        }
    }

    @Override
    public void onBeforeAllChanged() {
        if (isEnable()) {
            ForeachUtils.each(dataObservers, DataObserver::onBeforeAllChanged);
        }
    }

    @Override
    public void onAllChanged() {
        dirty();
        if (isEnable()) {
            ForeachUtils.each(dataObservers, DataObserver::onAllChanged);
        }
    }

    private boolean isEnable() {
        return getSemaphore().get() == 0;
    }

    public void enable() {
        getSemaphore().incrementAndGet();
    }

    public void disable() {
        getSemaphore().decrementAndGet();
    }

    public AtomicInteger getSemaphore() {
        return semaphore.get();
    }

    public void addDataObserver(DataObserver dataObserver) {
        dataObservers.add(dataObserver);
    }

    public void removeDataObserver(DataObserver dataObserver) {
        dataObservers.remove(dataObserver);
    }

    public void addDirtyAble(DirtyAble dirtyAble) {
        this.dirtyAbles.add(dirtyAble);
    }

    public void removeDirtyAble(DirtyAble dirtyAble) {
        this.dirtyAbles.remove(dirtyAble);
    }

    @Override
    public void dirty() {
        ForeachUtils.each(this.dirtyAbles, DirtyAble::dirty);
    }
}
