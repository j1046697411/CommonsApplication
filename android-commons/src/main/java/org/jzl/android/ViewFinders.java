package org.jzl.android;

import android.app.Activity;
import android.app.Dialog;
import android.util.SparseArray;
import android.view.View;

import org.jzl.lang.util.ObjectUtils;

import java.util.Objects;

public final class ViewFinders {

    private ViewFinders() {
    }

    public static ViewFinder finder(Activity activity) {
        return new ActivityViewFinder(activity);
    }

    public static ViewFinder finder(Dialog dialog) {
        return new DialogViewFinder(dialog);
    }

    public static ViewFinder finder(View root) {
        return new RootViewFinder(root);
    }

    public static ViewFinder cacheViewFinder(Activity activity) {
        return new CacheViewFinder(finder(activity));
    }

    public static ViewFinder cacheViewFinder(Dialog dialog) {
        return new CacheViewFinder(finder(dialog));
    }

    public static ViewFinder cacheViewFinder(View root) {
        return new CacheViewFinder(finder(root));
    }

    public static ViewFinder cacheViewFinder(ViewFinder viewFinder) {
        return viewFinder instanceof CacheViewFinder ? viewFinder : new CacheViewFinder(viewFinder);
    }

    private static class ActivityViewFinder implements ViewFinder {
        private Activity activity;

        ActivityViewFinder(Activity activity) {
            this.activity = Objects.requireNonNull(activity);
        }

        @Override
        public <V extends View> V findViewById(int id) {
            return activity.findViewById(id);
        }
    }

    private static class RootViewFinder implements ViewFinder {

        private View root;

        RootViewFinder(View root) {
            this.root = ObjectUtils.requireNonNull(root);
        }

        @Override
        public <V extends View> V findViewById(int id) {
            return root.findViewById(id);
        }
    }

    private static class DialogViewFinder implements ViewFinder {

        private Dialog dialog;

        DialogViewFinder(Dialog dialog) {
            this.dialog = ObjectUtils.requireNonNull(dialog);
        }

        @Override
        public <V extends View> V findViewById(int id) {
            return dialog.findViewById(id);
        }
    }

    private static class CacheViewFinder implements ViewFinder {

        private SparseArray<View> cacheViews;
        private ViewFinder sourceViewFinder;

        CacheViewFinder(ViewFinder sourceViewFinder) {
            this.sourceViewFinder = ObjectUtils.requireNonNull(sourceViewFinder);
            this.cacheViews = new SparseArray<>();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <V extends View> V findViewById(int id) {
            View view = cacheViews.get(id);
            if (view == null) {
                view = sourceViewFinder.findViewById(id);
                cacheViews.put(id, view);
            }
            return (V) view;
        }
    }

}
