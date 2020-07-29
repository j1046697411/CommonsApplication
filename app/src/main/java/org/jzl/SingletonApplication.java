package org.jzl;

import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;

public class SingletonApplication extends Application {
    private static SingletonApplication SIN;

    private WeakReference<Context> contextWeakReference;

    @Override
    public void onCreate() {
        super.onCreate();
        SIN = this;
    }


    public SingletonApplication getInstance(){
        return SingletonApplication.SIN;
    }
}
