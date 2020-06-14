package org.jzl.android.recyclerview.vh;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.ViewBinder;
import org.jzl.android.ViewFinder;
import org.jzl.android.provider.Provider;

public class CommonlyViewHolder extends RecyclerView.ViewHolder implements ViewFinder, Provider<ViewBinder>, DataBinderCallback {

    private ViewBinder viewBinder;

    public CommonlyViewHolder(@NonNull View itemView) {
        super(itemView);
        viewBinder = ViewBinder.bind(itemView);
    }

    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return viewBinder.findViewById(id);
    }

    @Override
    public ViewBinder provide() {
        return viewBinder;
    }

    @Override
    public void beforeBindViewHolder() {
    }

    @Override
    public void afterBindViewHolder() {
    }
}
