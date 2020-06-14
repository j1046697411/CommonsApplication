package org.jzl.android.recyclerview.vh;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.provider.ContextProvider;

public interface ViewHolderFactory<VH extends RecyclerView.ViewHolder> {

    VH createViewHolder(ContextProvider provider, int viewType, View itemView);


}
