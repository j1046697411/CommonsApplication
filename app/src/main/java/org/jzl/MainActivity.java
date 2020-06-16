package org.jzl;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jzl.android.recyclerview.builder.CommonlyRecyclerViewConfigurator;
import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.plugins.DividingLinePlugin;
import org.jzl.android.recyclerview.plugins.LayoutManagerPlugin;
import org.jzl.android.recyclerview.plugins.RefreshLoadMorePlugin;
import org.jzl.android.recyclerview.plugins.SectionPlugin;
import org.jzl.android.recyclerview.refresh.OnLoadMoreListener;
import org.jzl.android.recyclerview.refresh.OnRefreshListener;
import org.jzl.android.recyclerview.refresh.RefreshLayout;
import org.jzl.android.recyclerview.vh.CommonlyViewHolder;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        CommonlyRecyclerViewConfigurator.of("11111", "11111", "11111", "11111", "11111", "11111")
//                .itemTypes((position, data) -> position < 3 ? 1 : 0)
//                .itemViews(R.layout.item_test, 0, 1)
//                .dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data), 0, 1)
//                .layoutManager(contextProvider -> new LinearLayoutManager(contextProvider.provide(), LinearLayoutManager.VERTICAL, false))
//                .plugin(SectionPlugin.of(true, true, R.id.cb_test), 0, 1)
//                .plugin(LayoutManagerPlugin.linearLayoutManager())
//                .plugin(DividingLinePlugin.of(0xff00ff00, 10))
//                .plugin(RefreshLoadMorePlugin.of(new RefreshLayout() {
//                    @Override
//                    public void finishRefresh(int delay, boolean success, boolean noMoreData) {
//                        smartRefreshLayout.finishRefresh(delay, success, noMoreData);
//                    }
//
//                    @Override
//                    public void finishLoadMore(int delay, boolean success, boolean noMoreData) {
//                        smartRefreshLayout.finishLoadMore(delay, success, noMoreData);
//                    }
//
//                    @Override
//                    public void setRefreshListener(OnRefreshListener refreshListener) {
//                        smartRefreshLayout.setOnRefreshListener(refreshLayout -> refreshListener.onRefresh(this));
//                    }
//
//                    @Override
//                    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
//                        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreListener.onLoadMore(this));
//                    }
//                }, new Pages(), (request, isRefresh, callback) -> callback.finishedLoad(isRefresh, 0, true, request.page >= 10, Arrays.asList("page:" + request.page, "2", "3"))))
//                .bind(findViewById(R.id.rv_test));
//        bind();
        bind();
        bind2();
    }

    public void bind() {
        SmartRefreshLayout smartRefreshLayout = findViewById(R.id.srfl_test);
        //设置数据
        CommonlyRecyclerViewConfigurator.of("1", "2")
                //绑定数据类型
                .itemTypes((position, data) -> position % 2 == 0 ? 1 : 0)
                //对应的数据类型绑定数据
                .itemViews(R.layout.item_test, 0)
                //对应的数据类型绑定数据
                .itemViews(R.layout.item_test2, 1)
                //使用选择插件
                .plugin(SectionPlugin.of(true, true, R.id.cb_test), 0)
                //使用数据绑定插件
                .plugin(SetTextRecyclerViewPlugin.of(), 0, 1)
                .plugin(RefreshLoadMorePlugin.of(new RefreshLayout() {
                    @Override
                    public void finishRefresh(int delay, boolean success, boolean noMoreData) {
                        smartRefreshLayout.finishRefresh(delay, success, noMoreData);
                    }

                    @Override
                    public void finishLoadMore(int delay, boolean success, boolean noMoreData) {
                        smartRefreshLayout.finishLoadMore(delay, success, noMoreData);
                    }

                    @Override
                    public void setRefreshListener(OnRefreshListener refreshListener) {
                        smartRefreshLayout.setOnRefreshListener(refreshLayout -> refreshListener.onRefresh(this));
                    }

                    @Override
                    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
                        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> loadMoreListener.onLoadMore(this));
                    }
                }, new Pages(), (request, isRefresh, callback) -> {
                    callback.finishedLoad(isRefresh, 0, true, request.page > 10, Arrays.asList("1", "2", "3"));
                }))
                //绑定RecyclerView
                .bind(findViewById(R.id.rv_test));
    }

    public void bind2() {
        CommonlyRecyclerViewConfigurator.of("1", "2")
                .itemViews((layoutInflater, parent) -> {
                    TextView textView = new TextView(parent.getContext());
                    textView.setId(R.id.tv_test);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    return textView;
                })
                .plugin(SetTextRecyclerViewPlugin.of())
                //分割线插件
                .plugin(DividingLinePlugin.of(0xffff00ff, 5))
                //布局管理器插件
                .plugin(LayoutManagerPlugin.gridLayoutManager(2))
                .bind(findViewById(R.id.tv_test2));
    }

    public static class SetTextRecyclerViewPlugin implements RecyclerViewConfigurator.RecyclerViewPlugin<String, CommonlyViewHolder> {

        @Override
        public void setup(RecyclerViewConfigurator<String, CommonlyViewHolder> configurator, int... viewTypes) {
            configurator.dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data), viewTypes);
        }

        public static SetTextRecyclerViewPlugin of() {
            return new SetTextRecyclerViewPlugin();
        }
    }

    private static class Pages implements RefreshLoadMorePlugin.PageRequest<Pages> {

        private int page;

        @Override
        public Pages nextPage() {
            page += 1;
            return this;
        }

        @Override
        public Pages firstPage() {
            page = 1;
            return this;
        }
    }
}