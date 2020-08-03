package org.jzl.lang.util.datablcok;

import org.jzl.lang.util.ObjectUtils;

public final class DataBlockProviders {
    public static final int DEFAULT_BLOCK_ID = 1;

    private DataBlockProviders() {
    }

    public static <T> DataBlockProvider<T> dataBlockProvider(DataBlockFactory<T> dataBlockFactory, int defaultBlockId) {
        return new DataBlockProviderImpl<>(ObjectUtils.requireNonNull(dataBlockFactory), defaultBlockId);
    }

    public static <T> DataBlockProvider<T> dataBlockProvider(int defaultBlockId) {
        return dataBlockProvider(DataBlockImpl::new, defaultBlockId);
    }

    public static <T> DataBlockProvider<T> dataBlockProvider(){
        return dataBlockProvider(DataBlockProviders.DEFAULT_BLOCK_ID);
    }

}
