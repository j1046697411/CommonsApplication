package org.jzl.android;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

public class ViewBinder implements ViewFinder {

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
