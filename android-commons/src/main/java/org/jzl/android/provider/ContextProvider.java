package org.jzl.android.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import org.jzl.lang.fun.Consumer;
import org.jzl.lang.util.ObjectUtils;

@FunctionalInterface
public interface ContextProvider extends Provider<Context> {
    Consumer<Intent> EMPTY = intent -> {
    };

    @Override
    Context provide();

    static ContextProvider of(View view) {
        ObjectUtils.requireNonNull(view, "view");
        return view::getContext;
    }

    static ContextProvider of(Context context) {
        ObjectUtils.requireNonNull(context, "context");
        return () -> context;
    }

    default void jumpActivity(Class<? extends Activity> type, Consumer<Intent> consumer) {
        ObjectUtils.requireNonNull(type, "type");
        ObjectUtils.requireNonNull(consumer, "consumer");
        Context context = provide();
        Intent intent = new Intent(context, type);
        consumer.accept(intent);
        context.startActivity(intent);
    }

    default void jumpActivity(Class<? extends Activity> type) {
        jumpActivity(type, EMPTY);
    }

    default String getString(@StringRes int stringId, Object... formatArgs) {
        return provide().getString(stringId, formatArgs);
    }

    default Drawable getDrawable(@DrawableRes int id) {
        return provide().getDrawable(id);
    }

}
