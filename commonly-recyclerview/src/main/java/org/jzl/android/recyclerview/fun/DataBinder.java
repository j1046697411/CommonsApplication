package org.jzl.android.recyclerview.fun;

import androidx.recyclerview.widget.RecyclerView;

public interface DataBinder<T, VH extends RecyclerView.ViewHolder> {

    void bind(VH holder, T data);

}
