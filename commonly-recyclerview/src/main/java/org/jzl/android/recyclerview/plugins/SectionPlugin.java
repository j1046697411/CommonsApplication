package org.jzl.android.recyclerview.plugins;

import android.util.Log;
import android.widget.CompoundButton;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import org.jzl.android.ViewBinder;
import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.fun.DataClassifier;
import org.jzl.android.recyclerview.fun.DataClassifierBinder;
import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.android.recyclerview.fun.DataProviderBinder;
import org.jzl.android.recyclerview.provider.WrapDataProvider;
import org.jzl.android.recyclerview.vh.CommonlyViewHolder;
import org.jzl.android.recyclerview.wrap.CommonlyWrapper;
import org.jzl.android.recyclerview.wrap.EntityWrapper;
import org.jzl.lang.util.ArrayUtils;
import org.jzl.lang.util.ObjectUtils;

public class SectionPlugin<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH>, DataProviderBinder<T>, DataClassifierBinder<T> {

    public static final int KEY_SELECT = -1;

    private WrapDataProvider<T, EntityWrapper<T>> dataProvider;
    private DataClassifier<T> dataClassifier;

    private SelectBinder<T, VH> selectBinder;
    private boolean isSingleSelect;
    private boolean isSingleViewTypes;

    private SectionPlugin(boolean isSingleSelect, boolean isSingleViewTypes, SelectBinder<T, VH> selectBinder) {
        this.isSingleSelect = isSingleSelect;
        this.isSingleViewTypes = isSingleViewTypes;
        this.selectBinder = ObjectUtils.requireNonNull(selectBinder, "selectBinder");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        if (!configurator.isWrap()) {
            configurator.wrap(CommonlyWrapper::new);
        }
        configurator.bindDataClassifierBinder(this)
                .bindDataProviderBinder(this)
                .dataBinds((holder, data) -> {
                    EntityWrapper<T> entityWrapper = dataProvider.getWrapData(holder.getAdapterPosition());
                    selectBinder.bind(holder, data, isSelected(entityWrapper), selected -> {
                        Log.d("bind", "bind:" + selected);
                        if (selected && isSingleSelect) {
                            dataProvider.eachWrap((index, target) -> {
                                int dataType = dataClassifier.getItemType(index, target.wrap());
                                if (isSingleViewTypes && ArrayUtils.contains(viewTypes, dataType)) {
                                    this.selected(target, false);
                                } else if (!isSingleViewTypes && holder.getItemViewType() == dataType) {
                                    this.selected(target, false);
                                }
                            });
                        }
                        this.selected(entityWrapper, selected);
                    });
                }, viewTypes);
    }

    @Override
    public void bind(DataProvider<T> dataProvider) {
        this.dataProvider = (WrapDataProvider<T, EntityWrapper<T>>) dataProvider;
    }

    @Override
    public void bind(DataClassifier<T> dataClassifier) {
        this.dataClassifier = dataClassifier;
    }

    public interface SelectBinder<T, VH extends RecyclerView.ViewHolder> {

        void bind(VH holder, T data, boolean selected, Selector selector);

    }

    public interface Selector {

        void select(boolean selected);

    }

    public boolean isSelected(EntityWrapper<T> entityWrapper) {
        return entityWrapper.get(KEY_SELECT, false);
    }

    public void selected(EntityWrapper<T> entityWrapper, boolean selected) {
        entityWrapper.put(KEY_SELECT, selected);
    }

    public static <T, VH extends RecyclerView.ViewHolder> SectionPlugin<T, VH> of(boolean isSingleSelect, boolean isSingleViewTypes, SelectBinder<T, VH> selectBinder) {
        return new SectionPlugin<>(isSingleSelect, isSingleViewTypes, selectBinder);
    }

    public static <T, VH extends RecyclerView.ViewHolder> SectionPlugin<T, VH> of(SelectBinder<T, VH> selectBinder) {
        return of(false, false, selectBinder);
    }

    public static <T, VH extends RecyclerView.ViewHolder> SectionPlugin<T, VH> of(boolean isSingleSelect, boolean isSingleViewTypes, @IdRes int id) {
        return of(isSingleSelect, isSingleViewTypes, (holder, data, selected, selector) -> {
            int tag = 0x963158;
            ViewBinder binder;
            if (holder instanceof CommonlyViewHolder) {
                binder = ((CommonlyViewHolder) holder).provide();
            } else {
                binder = (ViewBinder) holder.itemView.getTag(tag);
                if (ObjectUtils.nonNull(binder)) {
                    binder = ViewBinder.bind(holder.itemView);
                    holder.itemView.setTag(tag, binder);
                }
            }
            binder.bindCheckedChangeListener(id, (CompoundButton.OnCheckedChangeListener) null);
            binder.setChecked(id, selected);
            binder.bindCheckedChangeListener(id, (buttonView, isChecked) -> selector.select(isChecked));
        });
    }

    public static <T, VH extends RecyclerView.ViewHolder> SectionPlugin<T, VH> of(@IdRes int id) {
        return of(false, false, id);
    }
}
