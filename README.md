# CommonlyAdapter
一个强大并且灵活的`RecyclerViewAdapter`，欢迎使用。  
（喜欢的可以**Star**一下）  
该适配器是基于`AndroidX`构建的，暂时不打算支持老版本的`RecyclerView`
CommonlyAdapter致力于`使用简单`、`可扩展`和`代码重用`，解决开发中常遇到的布局和数据绑定的问题。  
[一个使用简单、可扩展和代码重用的RecyclerViewAdapter](https://blog.csdn.net/qq_19326641/article/details/106747725)

[完整示例](./COMPLETE_EXAMPLE.md)

# 引入方式
```
implementation 'org.jzl.android:android-commons:0.0.1' #主要提供设置view的快捷方式
implementation 'org.jzl.android.recyclerview:commonly-recyclerview:0.0.1' #主项目
```

## 1、使用简单
#### 使用布局文件
```java_holder_method_tree
    CommonlyRecyclerViewConfigurator.of("1", "2")
        .itemViews(R.layout.item_test)
        .dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data))
        .bind(findViewById(R.id.rv_test));
```
#### 通过代码创建
```java_holder_method_tree
    CommonlyRecyclerViewConfigurator.of("1", "2")
        .itemViews((layoutInflater, parent) -> {
            TextView textView = new TextView(parent.getContext());
            textView.setId(R.id.tv_test);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return textView;
        })
        .dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data))
        .bind(findViewById(R.id.rv_test));
```
## 2、多布局使用
```java_holder_method_tree
    CommonlyRecyclerViewConfigurator.of("1", "2")
        .itemTypes((position, data) -> position % 2 == 0 ? 1 : 0)
        .itemViews(R.layout.item_test, 0)
        .itemViews(R.layout.item_test2, 1)
        .dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data), 0, 1)
        .bind(findViewById(R.id.rv_test));
```
## 3、选择器布局使用

```java_holder_method_tree
    CommonlyRecyclerViewConfigurator.of("1", "2")
        .itemTypes((position, data) -> position % 2 == 0 ? 1 : 0)
        .itemViews(R.layout.item_test, 0)
        .itemViews(R.layout.item_test2, 1)
        .dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data))
        .plugin(SectionPlugin.of(true, true, R.id.cb_test), 0)
        .bind(findViewById(R.id.rv_test));
```
## 4、通过插件(RecyclerViewPlugin<T, VH>)重用代码
```java_holder_method_tree
    public void bind(){
        CommonlyRecyclerViewConfigurator.of("1", "2")
                .itemTypes((position, data) -> position % 2 == 0 ? 1 : 0)
                .itemViews(R.layout.item_test, 0)
                .itemViews(R.layout.item_test2, 1)
                .plugin(SectionPlugin.of(true, true, R.id.cb_test), 0)
                .plugin(SetTextRecyclerViewPlugin.of(), 0, 1)
                .bind(findViewById(R.id.rv_test));
    }

    public void bind2(){
        CommonlyRecyclerViewConfigurator.of("1", "2")
                .itemViews((layoutInflater, parent) -> {
                    TextView textView = new TextView(parent.getContext());
                    textView.setId(R.id.tv_test);
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    return textView;
                })
                .plugin(SetTextRecyclerViewPlugin.of())
                .bind(findViewById(R.id.tv_test2));
    }

    public static class SetTextRecyclerViewPlugin implements RecyclerViewConfigurator.RecyclerViewPlugin<String, CommonlyViewHolder>{

        @Override
        public void setup(RecyclerViewConfigurator<String, CommonlyViewHolder> configurator, int... viewTypes) {
            configurator.dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data), viewTypes);
        }
        public static SetTextRecyclerViewPlugin of(){
            return new SetTextRecyclerViewPlugin();
        }
    }
```
## 5、加载更多插件
[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
使用的是第三方加载框架，感兴趣的可以关注一下上拉加载，下拉刷新做的还是不错的
#### 布局文件
```xml
    <com.scwang.smart.refresh.layout.SmartRefreshLayout
        android:id="@+id/srfl_test"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.scwang.smart.refresh.header.ClassicsHeader
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/rv_test"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_margin="10dp" />

        <com.scwang.smart.refresh.footer.ClassicsFooter
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
    </com.scwang.smart.refresh.layout.SmartRefreshLayout>
```
#### 实现代码
```java_holder_method_tree
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
            //刷新加载插件，暂时不提供布局实现，可以使用推荐的第三方加载刷新库，可以很简单实现封装，暂就不提供封装代码了
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
                //加载自己的数据
                callback.finishedLoad(isRefresh, 0, true, request.page > 10, Arrays.asList("1", "2", "3"));
            }))
            //绑定RecyclerView
            .bind(findViewById(R.id.rv_test));

```
## 6、其他插件

```java_holder_method_tree
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

```

## 7、插件扩展
这是分割线插件扩展，其他的插件扩展类似，可以很容易扩展出平时项目中常用的一些插件，
比如多个页面中都使用了RecyclerView，但是数据格式都一样的情况下就可以使用插件扩展出数据绑定的插件出来，
利用代码重用。这儿只是抛砖引玉，希望大家尽情发挥。
```java
public class DividingLinePlugin<T, VH extends RecyclerView.ViewHolder> implements RecyclerViewConfigurator.RecyclerViewPlugin<T, VH> {

    //分割线Drawable对象
    private Drawable dividingDrawable;

    private DividingLinePlugin(Drawable dividingDrawable) {
        this.dividingDrawable = ObjectUtils.requireNonNull(dividingDrawable, "dividingDrawable");
    }

    @Override
    public void setup(RecyclerViewConfigurator<T, VH> configurator, int... viewTypes) {
        //通过配置文件获取对应的contextProvider，recyclerView，layoutManager等对象，更具逻辑实现相应的分割线
        configurator.bind((contextProvider, recyclerView, layoutManager) -> {
            Context context = contextProvider.provide();
            //StaggeredGridLayoutManager和GridLayoutManager 添加横竖的风格线
            if (layoutManager instanceof StaggeredGridLayoutManager || layoutManager instanceof GridLayoutManager) {
                recyclerView.addItemDecoration(itemDecoration(context, dividingDrawable, StaggeredGridLayoutManager.VERTICAL));
                recyclerView.addItemDecoration(itemDecoration(context, dividingDrawable, StaggeredGridLayoutManager.HORIZONTAL));
                //LinearLayoutManager 更具布局管理器方向添加分割线
            } else if (layoutManager instanceof LinearLayoutManager) {
                recyclerView.addItemDecoration(itemDecoration(contextProvider.provide(), dividingDrawable, ((LinearLayoutManager) layoutManager).getOrientation()));
            }
        });
    }

    private RecyclerView.ItemDecoration itemDecoration(Context context, Drawable drawable, int orientation) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, orientation);
        itemDecoration.setDrawable(drawable);
        return itemDecoration;
    }

    public static <T, VH extends RecyclerView.ViewHolder> DividingLinePlugin<T, VH> of(Drawable dividingDrawable) {
        return new DividingLinePlugin<>(dividingDrawable);
    }

    public static <T, VH extends RecyclerView.ViewHolder> DividingLinePlugin<T, VH> of(@ColorInt int color, int width, int height) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setSize(width, height);
        return of(gradientDrawable);
    }

    public static <T, VH extends RecyclerView.ViewHolder> DividingLinePlugin<T, VH> of(@ColorInt int color, int size) {
        return of(color, size, size);
    }
}
```
```java
public static class SetTextRecyclerViewPlugin implements RecyclerViewConfigurator.RecyclerViewPlugin<String, CommonlyViewHolder>{

    @Override
    public void setup(RecyclerViewConfigurator<String, CommonlyViewHolder> configurator, int... viewTypes) {
        configurator.dataBinds((holder, data) -> holder.provide().setText(R.id.tv_test, data), viewTypes);
    }
    public static SetTextRecyclerViewPlugin of(){
        return new SetTextRecyclerViewPlugin();
    }
}
```
# Thanks
[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)  
[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)  
