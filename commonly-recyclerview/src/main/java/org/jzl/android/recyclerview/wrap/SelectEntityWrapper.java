package org.jzl.android.recyclerview.wrap;

public interface SelectEntityWrapper<E> extends EntityWrapper<E> {

    boolean isSelected();

    void selected(boolean selected);
}
