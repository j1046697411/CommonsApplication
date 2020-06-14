package org.jzl.android.recyclerview.fun;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemViewHolderListener<T, VH extends RecyclerView.ViewHolder> {

    void onItemViewHolderCreated(VH holder);

}
