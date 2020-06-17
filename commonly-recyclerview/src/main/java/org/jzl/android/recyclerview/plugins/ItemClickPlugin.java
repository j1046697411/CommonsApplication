package org.jzl.android.recyclerview.plugins;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.android.recyclerview.fun.DataProviderBinder;
import org.jzl.lang.util.ObjectUtils;

public class ItemClickPlugin<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH>, DataProviderBinder<T> {

    private OnItemClickListener<T, VH> itemClickListener;
    private DataProvider<T> dataProvider;
    private boolean isLongClick;

    public ItemClickPlugin(boolean isLongClick, OnItemClickListener<T, VH> itemClickListener) {
        this.isLongClick = isLongClick;
        this.itemClickListener = ObjectUtils.requireNonNull(itemClickListener, "itemClickListener");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        configurator.bindDataProviderBinder(this);
        configurator.viewHolders(holder -> {
            if (isLongClick) {
                View.OnLongClickListener listener = v -> {
                    itemClickListener.onItemClick(holder, v, dataProvider.getData(holder.getAdapterPosition()));
                    return true;
                };
                bindLongClickListeners(holder, listener);
            } else {
                View.OnClickListener listener = v -> itemClickListener.onItemClick(holder, v, dataProvider.getData(holder.getAdapterPosition()));
                bindClickListeners(holder, listener);
            }
        }, viewTypes);
    }

    private void bindClickListeners(VH holder, View.OnClickListener listener) {
        holder.itemView.setOnClickListener(listener);
    }

    private void bindLongClickListeners(VH holder, View.OnLongClickListener listener) {
        holder.itemView.setOnLongClickListener(listener);
    }

    @Override
    public void bind(DataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public interface OnItemClickListener<T, VH extends RecyclerView.ViewHolder> {
        void onItemClick(VH holder, View view, T data);
    }

    public static <T, VH extends RecyclerView.ViewHolder> ItemClickPlugin<T, VH> of(boolean isLongClick, OnItemClickListener<T, VH> itemClickListener) {
        return new ItemClickPlugin<>(isLongClick, itemClickListener);
    }

}
