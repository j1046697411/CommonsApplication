package org.jzl;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.jzl.android.recyclerview.builder.CommonlyRecyclerViewConfigurator;
import org.jzl.android.recyclerview.builder.RecyclerViewConfigurator;
import org.jzl.android.recyclerview.plugins.AnimationPlugin;
import org.jzl.android.recyclerview.plugins.DividingLinePlugin;
import org.jzl.android.recyclerview.plugins.LayoutManagerPlugin;
import org.jzl.android.recyclerview.plugins.RefreshLoadMorePlugin;
import org.jzl.android.recyclerview.plugins.SectionPlugin;
import org.jzl.android.recyclerview.refresh.OnLoadMoreListener;
import org.jzl.android.recyclerview.refresh.OnRefreshListener;
import org.jzl.android.recyclerview.refresh.RefreshLayout;
import org.jzl.android.recyclerview.vh.CommonlyViewHolder;
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

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private Handler mainHandler;
    private int[] colors = {
            0xff00ff00,
            0xffff0000,
            0xff00ffff
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        SmartRefreshLayout smartRefreshLayout = findViewById(R.id.srfl_test);
        CommonlyRecyclerViewConfigurator.of(new UserInfo("j1046697411", "https://b-ssl.duitang.com/uploads/item/201410/09/20141009224754_AswrQ.jpeg"))
                .itemTypes((position, data) -> position % 3)
                .itemViews(R.layout.item_test)
                .dataBinds((holder, data) -> {
                    holder.provide().setText(R.id.tv_username, data.username);
                    holder.provide().setTextColor(R.id.tv_username, colors[RandomUtils.random(colors.length)]);
                    Glide.with(this).load(data.headImage).into(holder.<ImageView>findViewById(R.id.iv_head));
                })
                .plugin(AnimationPlugin.ofSlideInRight(), 2)
                .plugin(AnimationPlugin.ofSlideInBottom(), 1)
                .plugin(AnimationPlugin.ofSlideInLeft(), 0)
                .plugin(DividingLinePlugin.of(Color.TRANSPARENT, 10))
                .plugin(LayoutManagerPlugin.gridLayoutManager(3))
                .plugin(SectionPlugin.of(false, false, R.id.cb_test))
                .plugin(RefreshLoadMorePlugin.of(executorService, mainHandler, new RefreshLayout() {
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
}