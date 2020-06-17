# 完整示例

### Android 示例代码
```java
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
                .plugin(SmartRefreshLayoutPlugin.of(true, executorService, mainHandler, smartRefreshLayout, new Pages(), (request, isRefresh, callback) -> {
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
        private boolean autoRefresh;

        public SmartRefreshLayoutPlugin(boolean autoRefresh, ExecutorService executorService, Handler mainHandler, SmartRefreshLayout smartRefreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
            this.autoRefresh = autoRefresh;
            this.executorService = ObjectUtils.requireNonNull(executorService, "executorService");
            this.mainHandler = ObjectUtils.requireNonNull(mainHandler, "mainHandler");
            this.smartRefreshLayout = ObjectUtils.requireNonNull(smartRefreshLayout, "smartRefreshLayout");
            this.request = ObjectUtils.requireNonNull(request, "request");
            this.dataLoader = ObjectUtils.requireNonNull(dataLoader, "dataLoader");
            this.callback = ObjectUtils.requireNonNull(callback, "callback");
        }

        @Override
        public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
            if (autoRefresh) {
                configurator.bind((contextProvider, recyclerView, layoutManager) -> {
                    smartRefreshLayout.autoRefresh();
                });
            }
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

        public static <R, T, VH extends RecyclerView.ViewHolder> SmartRefreshLayoutPlugin<R, T, VH> of(boolean autoRefresh, ExecutorService executorService, Handler mainHandler, SmartRefreshLayout smartRefreshLayout, R request, DataLoader<R, T> dataLoader, RefreshLoadMoreHelper.Callback<R> callback) {
            return new SmartRefreshLayoutPlugin<>(autoRefresh, executorService, mainHandler, smartRefreshLayout, request, dataLoader, callback);
        }

        public static <R extends RefreshLoadMorePlugin.PageRequest<R>, T, VH extends RecyclerView.ViewHolder> SmartRefreshLayoutPlugin<R, T, VH> of(boolean autoRefresh, ExecutorService executorService, Handler mainHandler, SmartRefreshLayout smartRefreshLayout, R request, DataLoader<R, T> dataLoader) {
            return of(autoRefresh, executorService, mainHandler, smartRefreshLayout, request, dataLoader, (isRefresh, request1) -> isRefresh ? request1.firstPage() : request1.nextPage());
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
```
### 服务器代码
```java
@SpringBootApplication
public class DemoApplication {
    private static final String[] headImages = {
            "https://b-ssl.duitang.com/uploads/item/201410/09/20141009224754_AswrQ.jpeg",
            "https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1803056350,344909414&fm=111&gp=0.jpg",
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3321238736,733069773&fm=26&gp=0.jpg",
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3621869950,2480486393&fm=26&gp=0.jpg",
            "https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3521319392,1160740190&fm=26&gp=0.jpg",
            "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2743353606,4180318799&fm=26&gp=0.jpg"
    };

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @RestController
    @RequestMapping("/demo")
    public static class DemoController {

        @RequestMapping("/listUsers")
        public ResultObject<List<UserInfo>> listUsers(int page, int pageSize) {
            ArrayList<UserInfo> userInfos = new ArrayList<>();
            for (int i = 0; i < pageSize; i++) {
                userInfos.add(this.randomUserInfo());
            }
            ResultObject<List<UserInfo>> resultObject = new ResultObject<>();
            resultObject.code = 200;
            resultObject.message = "ok";
            resultObject.data = userInfos;
            resultObject.hasNextPage = page < 10;
            return resultObject;

        }

        public UserInfo randomUserInfo() {
            UserInfo info = new UserInfo();
            info.username = StringRandomUtils.randomLowerString(10);
            info.headImage = headImages[RandomUtils.random(headImages.length)];
            return info;
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultObject<T> {
        private int code;
        private String message;
        private boolean hasNextPage;
        private T data;
    }

    @Data
    public static class UserInfo {

        private String username;
        private String headImage;

    }

}

```
