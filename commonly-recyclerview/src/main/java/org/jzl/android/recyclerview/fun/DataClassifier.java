package org.jzl.android.recyclerview.fun;

public interface DataClassifier<T> {

    int getItemType(int position, T data);

}
