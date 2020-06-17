package org.jzl.android.recyclerview.fun;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemViewAttachedToWindow<VH extends RecyclerView.ViewHolder> {

    void onViewAttachedToWindow(VH holder);
    
}
