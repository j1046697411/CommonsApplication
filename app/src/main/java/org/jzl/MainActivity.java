package org.jzl;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jzl.android.recyclerview.builder.CommonlyRecyclerViewConfigurator;
import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.fun.DataProvider;
import org.jzl.android.recyclerview.fun.DataProviderBinder;
import org.jzl.android.recyclerview.plugins.AnimationPlugin;
import org.jzl.android.recyclerview.plugins.DividingLinePlugin;
import org.jzl.android.recyclerview.plugins.EmptyLayoutPlugin;
import org.jzl.android.recyclerview.plugins.ItemClickPlugin;
import org.jzl.android.recyclerview.plugins.LayoutManagerPlugin;
import org.jzl.android.recyclerview.plugins.RefreshLoadMorePlugin;
import org.jzl.android.recyclerview.plugins.SectionPlugin;
import org.jzl.android.recyclerview.refresh.DataLoader;
import org.jzl.android.recyclerview.refresh.OnLoadMoreListener;
import org.jzl.android.recyclerview.refresh.OnRefreshListener;
import org.jzl.android.recyclerview.refresh.RefreshLayout;
import org.jzl.android.recyclerview.refresh.RefreshLoadMoreHelper;
import org.jzl.lang.util.ObjectUtils;
import org.jzl.lang.util.RandomUtils;
import org.jzl.lang.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements DataProviderBinder<MainActivity.UserInfo> {

    private ExecutorService executorService;
    private Handler mainHandler;
    private int[] colors = {
            0xff00ff00,
            0xffff0000,
            0xff00ffff
    };
    private DataProvider<UserInfo> dataProvider;

    @Override
    public void bind(DataProvider<UserInfo> dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        SmartRefreshLayout smartRefreshLayout = findViewById(R.id.srfl_test);
        CommonlyRecyclerViewConfigurator.<UserInfo>of()
                .itemTypes((position, data) -> position % 2)
                .itemViews(R.layout.item_test)
                .dataBinds((holder, data) -> {
                    holder.provide().setText(R.id.tv_username, data.username);
                    holder.provide().setTextColor(R.id.tv_username, colors[RandomUtils.random(colors.length)]);
                    Glide.with(this).load(data.headImage).into(holder.<ImageView>findViewById(R.id.iv_head));
                })
                .bindDataProviderBinder(this)
                .plugin(ItemClickPlugin.of(true, (holder, view, data) -> Toast.makeText(this, data.username, Toast.LENGTH_LONG).show()), 0)
                .plugin(ItemClickPlugin.of(false, (holder, view, data) -> Toast.makeText(this, data.username, Toast.LENGTH_LONG).show()), 1)
                .plugin(AnimationPlugin.ofSlideInRight(), 1)
                .plugin(AnimationPlugin.ofSlideInLeft(), 0)
                .plugin(DividingLinePlugin.of(Color.TRANSPARENT, 10))
                .plugin(LayoutManagerPlugin.gridLayoutManager(2))
                .plugin(SectionPlugin.of(false, false, R.id.cb_test))
                .plugin(EmptyLayoutPlugin.of(R.layout.item_test_empty))
                .plugin(SmartRefreshLayoutPlugin.of(executorService, mainHandler, smartRefreshLayout, new Pages(), (request, isRefresh, callback) -> {
                    String urlString = String.format(Locale.getDefault(), "http://192.168.137.1:8080/demo/listUsers?page=%d&pageSize=%d", request.page, request.pageSize);
                    try {
                        URL url = new URL(urlString);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        String res = StreamUtils.copyStreamToString(httpURLConnection.getInputStream());
                        ResultObject resultObject = JSON.parseObject(res, ResultObject.class);
                        callback.finishedLoad(isRefresh, 0, true, !resultObject.hasNextPage, resultObject.data);
                    } catch (IOException e) {
                        callback.finishedLoad(isRefresh, 0, false, true, new ArrayList<>());
                    }
                }))
                .bind(findViewById(R.id.rv_test));
    }

    public static class SmartRefreshLayoutPlugin<R, T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

        private ExecutorService executorService;
        private Handler mainHandler;
        private SmartRefreshLayout smartRefreshLayout;
        private R request;
        private DataLoader<R, T> dataLoader;
        private RefreshLoadMoreHelper.Callback<R> callback;

        public SmartRefreshLayoutPlugin(ExecutorService executorService, Handler mainHandler, SmartRefreshLayout smartRefreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
            this.executorService = ObjectUtils.requireNonNull(executorService, "executorService");
            this.mainHandler = ObjectUtils.requireNonNull(mainHandler, "mainHandler");
            this.smartRefreshLayout = ObjectUtils.requireNonNull(smartRefreshLayout, "smartRefreshLayout");
            this.request = ObjectUtils.requireNonNull(request, "request");
            this.dataLoader = ObjectUtils.requireNonNull(dataLoader, "dataLoader");
            this.callback = ObjectUtils.requireNonNull(callback, "callback");
        }

        @Override
        public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
            configurator.plugin(RefreshLoadMorePlugin.of(executorService, mainHandler, new RefreshLayout() {
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
            }, request, dataLoader, callback), viewTypes);
        }

        public static <R, T, VH extends RecyclerView.ViewHolder> SmartRefreshLayoutPlugin<R, T, VH> of(ExecutorService executorService, Handler mainHandler, SmartRefreshLayout smartRefreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
            return new SmartRefreshLayoutPlugin<>(executorService, mainHandler, smartRefreshLayout, request, dataLoader, callback);
        }

        public static <R extends RefreshLoadMorePlugin.PageRequest<R>, T, VH extends RecyclerView.ViewHolder> SmartRefreshLayoutPlugin<R, T, VH> of(ExecutorService executorService, Handler mainHandler, SmartRefreshLayout smartRefreshLayout, R request, DataLoader<R, T> dataLoader) {
            return of(executorService, mainHandler, smartRefreshLayout, request, dataLoader, (isRefresh, request1) -> isRefresh ? request1.firstPage() : request1.nextPage());
        }
    }


    private static class Pages implements RefreshLoadMorePlugin.PageRequest<Pages> {

        private int page;
        private int pageSize = 12;

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

    public static class ResultObject {
        private int code;
        private String message;
        private boolean hasNextPage;
        private List<UserInfo> data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isHasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }

        public List<UserInfo> getData() {
            return data;
        }

        public void setData(List<UserInfo> data) {
            this.data = data;
        }
    }

    public static class UserInfo {

        private String username;
        private String headImage;

        public UserInfo() {
        }

        public UserInfo(String username, String headImage) {
            this.username = username;
            this.headImage = headImage;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}