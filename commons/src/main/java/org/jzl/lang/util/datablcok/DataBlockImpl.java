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
    private final DataBlockProvider<T> dataBlockProvider;
    private final DataObserver dataObserver;
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
        if (isDirtyData.compareAndSet(true, false)) {
            this.startPosition = dataBlockProvider.getDataBlockStartPosition(this);
        }
        return startPosition;
    }

    @Override
    protected List<T> list() {
        return data;
    }

    @Override
    public boolean add(T t) {
        data.add(t);
        dataObserver.onInserted(size() - 1 + startPosition(), 1);
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
            dataObserver.onInserted(size + startPosition(), collection.size());
        }
        return is;
    }

    @Override
    public boolean addAll(int position, Collection<? extends T> collection) {
        boolean is = data.addAll(position, collection);
        if (is) {
            dataObserver.onInserted(position + startPosition(), collection.size());
        }
        return is;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean is = data.removeAll(collection);
        if (is) {
            dataObserver.onAllChanged();
        }
        return is;
    }

    @Override
    public void clear() {
        int size = size();
        data.clear();
        dataObserver.onRemoved(0, size);
    }

    @Override
    public T set(int position, T t) {
        T data = this.data.set(position, t);
        dataObserver.onChanged(position + startPosition());
        return data;
    }

    @Override
    public void add(int position, T t) {
        data.add(position, t);
        dataObserver.onInserted(position + startPosition(), 1);
    }

    @Override
    public T remove(int position) {
        T data = this.data.remove(position);
        if (ObjectUtils.nonNull(data)) {
            dataObserver.onRemoved(position + startPosition(), 1);
        }
        return data;
    }

    @Override
    public void move(int fromPosition, int toPosition) {
        CollectionUtils.move(this.data, fromPosition, toPosition);
        dataObserver.onMoved(fromPosition, toPosition);
    }

    @Override
    public void dirty() {
        isDirtyData.set(true);
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
