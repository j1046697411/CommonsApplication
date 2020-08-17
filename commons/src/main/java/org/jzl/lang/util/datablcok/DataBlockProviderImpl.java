package org.jzl.lang.util.datablcok;


import org.jzl.lang.util.CollectionUtils;
import org.jzl.lang.util.ForeachUtils;
import org.jzl.lang.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

class DataBlockProviderImpl<T> extends AbstractDataSource<T> implements DataBlockProvider<T>, DirtyAble {

    private final TreeSet<DataBlock<T>> dataBlocks = new TreeSet<>((dataBlock1, dataBlock2) -> {
        DataBlock.PositionType positionType1 = dataBlock1.getPositionType();
        DataBlock.PositionType positionType2 = dataBlock2.getPositionType();
        int sort = Integer.compare(positionType1.getSequence(), positionType2.getSequence());
        if (sort != 0) {
            return sort;
        }
        return Integer.compare(dataBlock1.getBlockId(), dataBlock2.getBlockId());
    });

    private DataObservers dataObservers = new DataObservers();

    private DataBlockFactory<T> dataBlockFactory;
    private AtomicBoolean isDirtyData = new AtomicBoolean(false);

    private final List<T> oldData = new ArrayList<>();
    private int defaultBlockId;
    private TreeSet<DataBlock<T>> queue;
    private Set<DataBlock<T>> unmodifiableSet;

    public DataBlockProviderImpl(DataBlockFactory<T> dataBlockFactory, int defaultBlockId) {
        this.dataBlockFactory = ObjectUtils.requireNonNull(dataBlockFactory, "dataBlockFactory");
        dataObservers.addDirtyAble(this);
        dataBlock(DataBlock.PositionType.CONTENT, defaultBlockId);
        this.defaultBlockId = defaultBlockId;
    }

    @Override
    public DataBlock<T> dataBlock(DataBlock.PositionType positionType, int blockId) {
        DataBlock<T> dataBlock = findDataBlock(positionType, blockId);
        if (ObjectUtils.isNull(dataBlock)) {
            dataBlock = dataBlockFactory.createDataBlock(blockId, positionType, this, dataObservers);
            this.dataBlocks.add(dataBlock);
        }
        return dataBlock;
    }

    private DataBlock<T> findDataBlock(DataBlock.PositionType positionType, int blockId) {
        return ForeachUtils.findByOne(dataBlocks, dataBlock -> isDataBlock(dataBlock, positionType, blockId));
    }

    private DataBlock<T> findDataBlockByPosition(int position) {
        for (DataBlock<T> dataBlock : this.dataBlocks) {
            int dataBlockSize = dataBlock.size();
            if (position < dataBlockSize) {
                return dataBlock;
            }
            position -= dataBlockSize;
        }
        return null;
    }

    private DataBlock<T> lastDataBlock() {
        return dataBlocks.last();
    }

    public DataBlock<T> lastContentDataBlock() {
        if (ObjectUtils.isNull(queue)) {
            queue = new TreeSet<>(this.dataBlocks);
        } else {
            queue.clear();
            queue.addAll(this.dataBlocks);
        }
        while (!queue.isEmpty()) {
            DataBlock<T> dataBlock = queue.pollLast();
            if (ObjectUtils.nonNull(dataBlock) && dataBlock.getPositionType() == DataBlock.PositionType.CONTENT) {
                return dataBlock;
            }
        }
        return null;
    }

    @Override
    public Set<DataBlock<T>> dataBlocks() {
        if (ObjectUtils.isNull(unmodifiableSet)) {
            unmodifiableSet = Collections.unmodifiableSet(this.dataBlocks);
        }
        return unmodifiableSet;
    }

    @Override
    public int getDataBlockStartPosition(DataBlock<T> dataBlock) {
        int startPosition = 0;
        for (DataBlock<T> db : dataBlocks) {
            if (db == dataBlock) {
                return startPosition;
            }
            startPosition += db.size();
        }
        return startPosition;
    }

    @Override
    public void addDataObserver(DataObserver dataObserver) {
        dataObservers.addDataObserver(dataObserver);
    }

    @Override
    public void removeDataObserver(DataObserver dataObserver) {
        dataObservers.removeDataObserver(dataObserver);
    }

    @Override
    public void addDirtyAble(DirtyAble dirtyAble) {
        dataObservers.addDirtyAble(dirtyAble);
    }

    @Override
    public void removeDirtyAble(DirtyAble dirtyAble) {
        dataObservers.removeDirtyAble(dirtyAble);
    }

    private void enableDataObserver() {
        dataObservers.enable();
    }

    private void disableDataObserver() {
        dataObservers.disable();
    }

    @Override
    public DataBlock<T> defaultDataBlock() {
        return dataBlock(DataBlock.PositionType.CONTENT, defaultBlockId);
    }

    @Override
    @SafeVarargs
    public final void addAll(DataBlock.PositionType positionType, int blockId, T... data) {
        dataBlock(positionType, blockId).addAll(data);
    }

    @Override
    public void addAll(DataBlock.PositionType positionType, int blockId, Collection<T> collection) {
        dataBlock(positionType, blockId).addAll(collection);
    }

    @Override
    @SafeVarargs
    public final void addAllToContent(T... data) {
        lastContentDataBlock().addAll(data);
    }

    @Override
    public void addAllToContent(Collection<T> collection) {
        lastContentDataBlock().addAll(collection);
    }

    @Override
    public DataBlock<T> removeDataBlock(DataBlock.PositionType positionType, int blockId) {
        DataBlock<T> dataBlock = findDataBlock(positionType, blockId);
        if (CollectionUtils.nonEmpty(dataBlock) && removeDataBlock(dataBlock)) {
            return dataBlock;
        }
        return null;
    }

    @Override
    public boolean removeDataBlock(DataBlock<T> dataBlock) {
        if (CollectionUtils.nonEmpty(dataBlock)) {
            int startPosition = dataBlock.startPosition();
            int size = dataBlock.size();
            DataBlock<T> retDataBlock = ForeachUtils.removeByOne(this.dataBlocks, target -> target == dataBlock);
            if (CollectionUtils.nonEmpty(retDataBlock)) {
                retDataBlock.disassociate();
                dataObservers.onRemoved(startPosition, size);
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeDataBlockByPositionType(DataBlock.PositionType positionType) {
        dataObservers.onBeforeAllChanged();
        ForeachUtils.remove(this.dataBlocks, dataBlock -> {
            if (dataBlock.getPositionType() == positionType) {
                dataBlock.disassociate();
                return true;
            }
            return false;
        });
        dataObservers.onAllChanged();
    }

    @Override
    public DataBlock<T> findDataBlockByIndex(int index) {
        return findDataBlockByPosition(index);
    }

    public T set(int position, T t) {
        DataBlock<T> dataBlock = findDataBlockByPosition(position);
        if (ObjectUtils.nonNull(dataBlock)) {
            return dataBlock.set(position - dataBlock.startPosition(), t);
        }
        return t;
    }

    @Override
    public boolean add(T object) {
        DataBlock<T> dataBlock = lastDataBlock();
        if (ObjectUtils.nonNull(dataBlock)) {
            return dataBlock.add(object);
        }
        return false;
    }

    @Override
    public void add(int position, T object) {
        DataBlock<T> dataBlock = findDataBlockByPosition(position);
        if (ObjectUtils.nonNull(dataBlock)) {
            dataBlock.add(position - dataBlock.startPosition(), object);
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        if (CollectionUtils.nonEmpty(collection)) {
            DataBlock<T> dataBlock = lastDataBlock();
            return dataBlock.addAll(collection);
        }
        return false;
    }

    @Override
    public boolean addAll(int position, Collection<? extends T> collection) {
        DataBlock<T> dataBlock = findDataBlockByPosition(position);
        if (ObjectUtils.nonNull(dataBlock)) {
            return dataBlock.addAll(position - dataBlock.startPosition(), collection);
        }
        return false;
    }

    @Override
    public T remove(int position) {
        DataBlock<T> dataBlock = findDataBlockByPosition(position);
        if (ObjectUtils.nonNull(dataBlock)) {
            dataBlock.remove(position - dataBlock.startPosition());
        }
        return null;
    }

    @Override
    public boolean remove(Object object) {
        if (ObjectUtils.nonNull(object)) {
            for (DataBlock<T> dataBlock : dataBlocks) {
                int index = dataBlock.indexOf(object);
                if (index != -1) {
                    dataBlock.remove(index);
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean removeAll(Collection<?> collection) {
        dataObservers.onBeforeAllChanged();
        disableDataObserver();
        for (DataBlock<T> dataBlock : dataBlocks) {
            dataBlock.removeAll(collection);
        }
        enableDataObserver();
        dataObservers.onAllChanged();
        return true;
    }

    @Override
    public void clear() {
        int size = size();
        disableDataObserver();
        for (DataBlock<T> dataBlock : dataBlocks) {
            dataBlock.clear();
        }
        dataBlocks.clear();
        oldData.clear();
        enableDataObserver();
        dataObservers.onRemoved(0, size - 1);
    }

    protected List<T> list() {
        updateOldData();
        return oldData;
    }

    public void dirty() {
        isDirtyData.set(true);
    }

    private void updateOldData() {
        if (isDirtyData.compareAndSet(true, false)) {
            synchronized (oldData) {
                oldData.clear();
                for (DataBlock<T> dataBlock : dataBlocks) {
                    oldData.addAll(dataBlock.snapshot());
                }
            }
        }
    }

    @Override
    public void move(int fromPosition, int toPosition) {
        disableDataObserver();//关闭事件
        CollectionUtils.move(this, fromPosition, toPosition);
        enableDataObserver();//开启事件
        dataObservers.onMoved(fromPosition, toPosition);
    }

    private boolean isDataBlock(DataBlock<T> dataBlock, DataBlock.PositionType positionType, int dataBlockId) {
        return ObjectUtils.nonNull(dataBlock) && dataBlock.getBlockId() == dataBlockId && dataBlock.getPositionType() == positionType;
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
        DataBlockProviderImpl<?> that = (DataBlockProviderImpl<?>) o;
        return Objects.equals(dataBlocks, that.dataBlocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataBlocks);
    }
}
