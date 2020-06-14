package org.jzl.android.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.lang.util.ObjectUtils;

public class CommonlyItemTouchHelper<T> extends ItemTouchHelper.Callback {

    private DataProvider<T> dataProvider;
    private int dragFlags;
    private int swipeFlags;

    public CommonlyItemTouchHelper(DataProvider<T> dataProvider, int dragFlags, int swipeFlags) {
        this.dataProvider = ObjectUtils.requireNonNull(dataProvider, "dataProvider");
        this.dragFlags = dragFlags;
        this.swipeFlags = swipeFlags;
    }

    public CommonlyItemTouchHelper(DataProvider<T> dataProvider) {
        this(dataProvider, ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int dragFlags = 0;
        int swipeFlags = 0;
        if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
            swipeFlags = 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
            } else {
                dragFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
                swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
        }
        return makeMovementFlags(dragFlags & this.dragFlags, swipeFlags & this.swipeFlags);

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int position = viewHolder.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();
        this.dataProvider.move(position, targetPosition);
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

}
