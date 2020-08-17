package org.jzl.android;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import org.jzl.lang.fun.Consumer;
import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.ObjectUtils;

public final class ViewBinder implements ViewFinder {

    private ViewFinder finder;

    private ViewBinder(ViewFinder finder) {
        this.finder = finder;
    }

    public ViewBinder setText(@IdRes int id, @StringRes int stringId) {
        this.<TextView>findViewById(id).setText(stringId);
        return this;
    }

    public ViewBinder setText(@IdRes int id, CharSequence text) {
        this.<TextView>findViewById(id).setText(text);
        return this;
    }

    public ViewBinder setTextColor(@IdRes int id, @ColorInt int color) {
        this.<TextView>findViewById(id).setTextColor(color);
        return this;
    }

    public ViewBinder setBackground(@IdRes int id, Drawable background) {
        this.findViewById(id).setBackground(background);
        return this;
    }

    public ViewBinder setBackground(@IdRes int id, @DrawableRes int backgroundId) {
        findViewById(id).setBackgroundResource(backgroundId);
        return this;
    }

    public ViewBinder setBackgroundColor(@IdRes int id, @ColorInt int colorId) {
        findViewById(id).setBackgroundColor(colorId);
        return this;
    }

    public ViewBinder setImageResource(@IdRes int id, @DrawableRes int resId) {
        this.<ImageView>findViewById(id).setImageResource(resId);
        return this;
    }

    public ViewBinder setImageBitmap(@IdRes int id, Bitmap bitmap) {
        this.<ImageView>findViewById(id).setImageBitmap(bitmap);
        return this;
    }

    public ViewBinder setImageDrawable(@IdRes int id, Drawable drawable) {
        this.<ImageView>findViewById(id).setImageDrawable(drawable);
        return this;
    }

    public ViewBinder setVisibility(@IdRes int id, int visibility) {
        findViewById(id).setVisibility(visibility);
        return this;
    }

    public ViewBinder setChecked(@IdRes int id, boolean checked) {
        this.<CompoundButton>findViewById(id).setChecked(checked);
        return this;
    }

    public ViewBinder bindClickListener(@IdRes int id, View.OnClickListener listener) {
        findViewById(id).setOnClickListener(listener);
        return this;
    }

    public ViewBinder bindCheckedChangeListener(@IdRes int id, CompoundButton.OnCheckedChangeListener listener) {
        this.<CompoundButton>findViewById(id).setOnCheckedChangeListener(listener);
        return this;
    }

    public ViewBinder bindLongClickListener(@IdRes int id, View.OnLongClickListener clickListener) {
        this.findViewById(id).setOnLongClickListener(clickListener);
        return this;
    }

    public ViewBinder bindClickListeners(View.OnClickListener listener, @IdRes int... ids) {
        if (ArrayUtils.nonEmpty(ids)) {
            for (int id : ids) {
                this.findViewById(id).setOnClickListener(listener);
            }
        }
        return this;
    }

    public ViewBinder setClickable(@IdRes int id, boolean clickable){
        findViewById(id).setClickable(clickable);
        return this;
    }

    public ViewBinder setFocusable(@IdRes int id, boolean focusable){
        findViewById(id).setFocusable(focusable);
        return this;
    }

    public ViewBinder addOnLayoutChangeListener(@IdRes int id, View.OnLayoutChangeListener layoutChangeListener){
        if (ObjectUtils.nonNull(layoutChangeListener)){
            findViewById(id).addOnLayoutChangeListener(layoutChangeListener);
        }
        return this;
    }

    public ViewBinder addOnAttachStateChangeListener(@IdRes int id, View.OnAttachStateChangeListener attachStateChangeListener){
        if (ObjectUtils.nonNull(attachStateChangeListener)){
            findViewById(id).addOnAttachStateChangeListener(attachStateChangeListener);
        }
        return this;
    }

    @SuppressWarnings("all")
    public <LP extends ViewGroup.LayoutParams> ViewBinder updateLayoutParams(@IdRes int id, Consumer<LP> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            ViewGroup viewGroup = findViewById(id);
            LP layoutParams = (LP) viewGroup.getLayoutParams();
            consumer.accept(layoutParams);
            viewGroup.setLayoutParams(layoutParams);
        }
        return this;
    }

    public ViewBinder bindCheckedChangeListeners(CompoundButton.OnCheckedChangeListener listener, @IdRes int... ids) {
        if (ArrayUtils.nonEmpty(ids)) {
            for (int id : ids) {
                this.<CompoundButton>findViewById(id).setOnCheckedChangeListener(listener);
            }
        }
        return this;
    }

    public ViewBinder bindLongClickListeners(View.OnLongClickListener longClickListener, @IdRes int... ids) {
        if (ArrayUtils.nonEmpty(ids)) {
            for (int id : ids) {
                findViewById(id).setOnLongClickListener(longClickListener);
            }
        }
        return this;
    }

    public ViewBinder bind(@IdRes int id, Consumer<ViewBinder> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        return bindView(id, view -> {
            ViewBinder viewBinder = (ViewBinder) view.getTag(R.id.tag_view_binder);
            if (ObjectUtils.isNull(viewBinder)) {
                viewBinder = ViewBinder.bind(view);
                view.setTag(R.id.tag_view_binder, viewBinder);
            }
            consumer.accept(viewBinder);
        });
    }

    public <V extends View> ViewBinder bindView(@IdRes int id, Consumer<V> consumer) {
        if (ObjectUtils.nonNull(consumer)) {
            consumer.accept(ObjectUtils.requireNonNull(this.findViewById(id), "view"));
        }
        return this;
    }

    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return finder.findViewById(id);
    }

    public static ViewBinder bind(ViewFinder viewFinder) {
        return new ViewBinder(ViewFinders.cacheViewFinder(viewFinder));
    }

    public static ViewBinder bind(View view) {
        return new ViewBinder(ViewFinders.cacheViewFinder(view));
    }

    public static ViewBinder bind(Activity activity) {
        return new ViewBinder(ViewFinders.cacheViewFinder(activity));
    }

    public static ViewBinder bind(Dialog dialog) {
        return new ViewBinder(ViewFinders.cacheViewFinder(dialog));
    }
}
