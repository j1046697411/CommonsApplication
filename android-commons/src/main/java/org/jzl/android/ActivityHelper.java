package org.jzl.android;

import android.app.Activity;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;

import org.jzl.android.provider.ContextProvider;
import org.jzl.lang.fun.Consumer;
import org.jzl.lang.fun.Function;
import org.jzl.lang.util.ObjectUtils;

public final class ActivityHelper implements ViewFinder {

    private Activity activity;
    private ViewBinder viewBinder;
    private Application application;
    private ContextProvider contextProvider;
    private LayoutInflater layoutInflater;


    private ActivityHelper(Activity activity) {
        ObjectUtils.requireNonNull(activity, "activity");
        this.activity = activity;
        viewBinder = ViewBinder.bind(activity);
        contextProvider = ContextProvider.of(activity);
        layoutInflater = activity.getLayoutInflater();
    }

    public ActivityHelper layoutContent(Function<LayoutInflater, View> function) {
        ObjectUtils.requireNonNull(function, "function");
        activity.setContentView(function.apply(layoutInflater));
        return this;
    }

    public ActivityHelper layoutContext(@LayoutRes int layoutId) {
        activity.setContentView(layoutId);
        return this;
    }

    public ActivityHelper bindData(Consumer<ViewBinder> consumer) {
        ObjectUtils.requireNonNull(consumer, "consumer");
        consumer.accept(viewBinder);
        return this;
    }

    @Override
    public <V extends View> V findViewById(int id) {
        return viewBinder.findViewById(id);
    }
}
