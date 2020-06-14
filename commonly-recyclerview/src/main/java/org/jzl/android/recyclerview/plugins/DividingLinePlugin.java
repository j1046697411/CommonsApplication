package org.jzl.android.recyclerview.plugins;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.lang.util.ObjectUtils;

public class DividingLinePlugin<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

    //分割线Drawable对象
    private Drawable dividingDrawable;

    private DividingLinePlugin(Drawable dividingDrawable) {
        this.dividingDrawable = ObjectUtils.requireNonNull(dividingDrawable, "dividingDrawable");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        //通过配置文件获取对应的contextProvider，recyclerView，layoutManager等对象，更具逻辑实现相应的分割线
        configurator.bind((contextProvider, recyclerView, layoutManager) -> {
            Context context = contextProvider.provide();
            //StaggeredGridLayoutManager和GridLayoutManager 添加横竖的风格线
            if (layoutManager instanceof StaggeredGridLayoutManager || layoutManager instanceof GridLayoutManager) {
                recyclerView.addItemDecoration(itemDecoration(context, dividingDrawable, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.addItemDecoration(itemDecoration(context, dividingDrawable, StaggeredGridLayoutManager.HORIZONTAL));
                //LinearLayoutManager 更具布局管理器方向添加分割线
            } else if (layoutManager instanceof LinearLayoutManager) {
                recyclerView.addItemDecoration(itemDecoration(contextProvider.provide(), dividingDrawable, ((LinearLayoutManager) layoutManager).getOrientation()));
            }
        });
    }

    private RecyclerView.ItemDecoration itemDecoration(Context context, Drawable drawable, int orientation) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, orientation);
        itemDecoration.setDrawable(drawable);
        return itemDecoration;
    }

    public static <T, VH extends RecyclerView.ViewHolder> DividingLinePlugin<T, VH> of(Drawable dividingDrawable) {
        return new DividingLinePlugin<>(dividingDrawable);
    }

    public static <T, VH extends RecyclerView.ViewHolder> DividingLinePlugin<T, VH> of(@ColorInt int color, int width, int height) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setSize(width, height);
        return of(gradientDrawable);
    }

    public static <T, VH extends RecyclerView.ViewHolder> DividingLinePlugin<T, VH> of(@ColorInt int color, int size) {
        return of(color, size, size);
    }
}
