package org.jzl.android;

import android.view.View;

import androidx.annotation.IdRes;

public interface ViewFinder {
    <V extends View> V findViewById(@IdRes int id);
}
