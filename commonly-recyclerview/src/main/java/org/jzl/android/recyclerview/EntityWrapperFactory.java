package org.jzl.android.recyclerview;

import org.jzl.android.recyclerview.wrap.EntityWrapper;

public interface EntityWrapperFactory<E, W extends EntityWrapper<E>> {

    W createEntityWrapper(E entity);

}
