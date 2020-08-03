package org.jzl.lang.util.datablcok;


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractDataSource<T> extends AbstractList<T> implements DataSource<T> {

    public AbstractDataSource() {
    }

    @Override
    public List<T> snapshot() {
        return new ArrayList<>(list());
    }

    @Override
    public int size() {
        return list().size();
    }

    @Override
    public boolean isEmpty() {
        return list().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list().contains(o);
    }

    @Override
    public Object[] toArray() {
        return list().toArray();
    }

    @Override
    @SuppressWarnings("all")
    public <T1> T1[] toArray(T1[] t1s) {
        return list().toArray(t1s);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return list().containsAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return list().retainAll(collection);
    }

    @Override
    public T get(int position) {
        return list().get(position);
    }


    @Override
    public int indexOf(Object o) {
        return list().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list().lastIndexOf(o);
    }

    protected abstract List<T> list();

}
