package org.jzl.android.recyclerview.util;

import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.ViewBinder;
import org.jzl.android.recyclerview.vh.CommonlyViewHolder;
import org.jzl.lang.util.ObjectUtils;

public class ViewBinderUtils {

    private static final int TAG_VIEW_BINDER = 0x963158;

    private ViewBinderUtils() {
    }

    public static <VH extends RecyclerView.ViewHolder> ViewBinder getViewBinder(VH vh) {
        ObjectUtils.requireNonNull(vh, "ViewHolder");
        if (vh instanceof CommonlyViewHolder) {
            return ((CommonlyViewHolder) vh).provide();
        } else {
            ViewBinder binder = (ViewBinder) vh.itemView.getTag(TAG_VIEW_BINDER);
            if (ObjectUtils.isNull(binder)) {
                binder = ViewBinder.bind(vh.itemView);
                vh.itemView.setTag(TAG_VIEW_BINDER, binder);
            }
            return binder;
        }

    }

}
