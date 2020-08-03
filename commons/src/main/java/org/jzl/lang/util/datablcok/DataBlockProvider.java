package org.jzl.lang.util.datablcok;

import java.util.Collection;
import java.util.List;

public interface DataBlockProvider<T> extends DataSource<T> {

    DataBlock<T> dataBlock(DataBlock.PositionType positionType, int blockId);

    int getDataBlockStartPosition(DataBlock<T> dataBlock);

    List<T> snapshot();

    void addDataObserver(DataObserver dataObserver);

    void removeDataObserver(DataObserver dataObserver);

    void addDirtyAble(DirtyAble dirtyAble);

    void removeDirtyAble(DirtyAble dirtyAble);

    void enableDataObserver();

    void disableDataObserver();

    DataBlock<T> defaultDataBlock();

    DataBlock<T> lastContentDataBlock();

    @SuppressWarnings("all")
    void addAll(DataBlock.PositionType positionType, int blockId, T... data);

    void addAll(DataBlock.PositionType positionType, int blockId, Collection<T> collection);

    @SuppressWarnings("all")
    void addAllToContent(T... data);

    void addAllToContent(Collection<T> collection);

}
