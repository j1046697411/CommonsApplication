package org.jzl.android.provider;

import android.content.Context;
import android.view.View;

import org.jzl.lang.util.ObjectUtils;

public interface ContextProvider extends Provider<Context> {

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
}
