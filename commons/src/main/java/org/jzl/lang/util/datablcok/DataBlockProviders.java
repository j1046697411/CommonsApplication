package org.jzl.lang.util.datablcok;

import org.jzl.lang.util.ObjectUtils;

public final class DataBlockProviders {
    private DataBlockProviders() {
    }

    public static <T> DataBlockProvider<T> dataBlockProvider(DataBlockFactory<T> dataBlockFactory, int defaultBlockId) {
        return new DataBlockProviderImpl<>(ObjectUtils.requireNonNull(dataBlockFactory), defaultBlockId);
    }

    public static <T> DataBlockProvider<T> dataBlockProvider(int defaultBlockId) {
        return dataBlockProvider(DataBlockImpl::new, defaultBlockId);
    }

}
