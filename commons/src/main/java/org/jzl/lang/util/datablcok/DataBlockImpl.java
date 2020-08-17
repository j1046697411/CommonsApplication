package org.jzl.lang.util.datablcok;


import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

class DataBlockImpl<T> extends AbstractDataSource<T> implements DataBlock<T>, DataSource<T>, DirtyAble {

    private final int blockId;
    private final PositionType positionType;
    private final List<T> data = new ArrayList<>();
    private DataBlockProvider<T> dataBlockProvider;
    private DataObserver dataObserver;
    private int startPosition = 0;
    private AtomicBoolean isDirtyData = new AtomicBoolean(false);

    public DataBlockImpl(int blockId, PositionType positionType, DataBlockProvider<T> dataBlockProvider, DataObserver dataObserver) {
        this.blockId = blockId;
        this.positionType = positionType;
        this.dataBlockProvider = ObjectUtils.requireNonNull(dataBlockProvider, "dataBlockProvider");
        this.dataObserver = ObjectUtils.requireNonNull(dataObserver, "dataObserver");
        dataBlockProvider.addDirtyAble(this);
    }

    @Override
    public int getBlockId() {
        return blockId;
    }

    @Override
    public PositionType getPositionType() {
        return positionType;
    }

    @Override
    public int startPosition() {
        if (isDirtyData.compareAndSet(true, false) && ObjectUtils.nonNull(dataBlockProvider)) {
            this.startPosition = dataBlockProvider.getDataBlockStartPosition(this);
        }
        return startPosition;
    }

    @Override
    public void disassociate() {
        this.dataObserver = null;
        this.dataBlockProvider.removeDirtyAble(this);
        this.dataBlockProvider = null;
        this.startPosition = 0;
    }

    @Override
    protected List<T> list() {
        return data;
    }

    @Override
    public boolean add(T t) {
        data.add(t);
        onInserted(size() - 1 + startPosition(), 1);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = data.indexOf(o);
        if (index != -1) {
            return remove(index) != null;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        int size = size();
        boolean is = data.addAll(collection);
        if (is) {
            onInserted(size + startPosition(), collection.size());
        }
        return is;
    }

    @Override
    public boolean addAll(int position, Collection<? extends T> collection) {
        boolean is = data.addAll(position, collection);
        if (is) {
            onInserted(position + startPosition(), collection.size());
        }
        return is;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        onBeforeAllChanged();
        boolean is = data.removeAll(collection);
        if (is) {
            onAllChanged();
        }
        return is;
    }

    @Override
    public void clear() {
        int size = size();
        data.clear();
        onRemoved(startPosition(), size);
    }

    @Override
    public T set(int position, T t) {
        T data = this.data.set(position, t);
        onChanged(position + startPosition());
        return data;
    }

    @Override
    public void add(int position, T t) {
        data.add(position, t);
        onInserted(position + startPosition(), 1);
    }

    @Override
    public T remove(int position) {
        T data = this.data.remove(position);
        if (ObjectUtils.nonNull(data)) {
            onRemoved(position + startPosition(), 1);
        }
        return data;
    }

    @Override
    public void move(int fromPosition, int toPosition) {
        CollectionUtils.move(this.data, fromPosition, toPosition);
        onMoved(fromPosition + startPosition(), toPosition + startPosition());
    }

    @Override
    public void dirty() {
        isDirtyData.set(true);
    }

    private void onInserted(int position, int count) {
        if (ObjectUtils.nonNull(dataObserver)) {
            dataObserver.onInserted(position, count);
        }
    }

    private void onChanged(int position) {
        if (ObjectUtils.nonNull(dataObserver)) {
            dataObserver.onChanged(position);
        }
    }

    private void onAllChanged() {
        if (ObjectUtils.nonNull(dataObserver)) {
            dataObserver.onAllChanged();
        }
    }

    private void onBeforeAllChanged(){
        if (ObjectUtils.nonNull(dataObserver)){
            dataObserver.onBeforeAllChanged();
        }
    }

    private void onRemoved(int position, int count) {
        if (ObjectUtils.nonNull(dataObserver)) {
            dataObserver.onRemoved(position, count);
        }
    }

    private void onMoved(int fromPosition, int toPosition) {
        if (ObjectUtils.nonNull(dataObserver)) {
            dataObserver.onMoved(fromPosition, toPosition);
        }
    }

    @Override
    public String toString() {
        return list().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DataBlockImpl<?> dataBlock = (DataBlockImpl<?>) o;
        return blockId == dataBlock.blockId &&
                positionType == dataBlock.positionType &&
                Objects.equals(data, dataBlock.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), blockId, positionType, data);
    }
}
