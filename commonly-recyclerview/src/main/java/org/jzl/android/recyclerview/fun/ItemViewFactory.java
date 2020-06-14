package org.jzl.android.recyclerview.fun;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface ItemViewFactory {

    View createItemView(LayoutInflater layoutInflater, ViewGroup parent);

}
