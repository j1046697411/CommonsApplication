package org.jzl.lang.util.datablcok;

public interface DataBlockFactory<T> {
    DataBlock<T> createDataBlock(int blockId, DataBlock.PositionType positionType, DataBlockProvider<T> dataBlockProvider, DataObservers dataObserver);
}
