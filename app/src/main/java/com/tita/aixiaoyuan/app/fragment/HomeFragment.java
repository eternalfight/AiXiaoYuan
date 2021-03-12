package com.tita.aixiaoyuan.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tita.aixiaoyuan.Adapter.ImageResourceAdapter;
import com.tita.aixiaoyuan.Adapter.TagFragmentAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.viewHolder.ImageResourceViewHolder;
import com.tita.aixiaoyuan.model.EventMsg;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.ui.SearchActivity;
import com.tita.aixiaoyuan.utils.RxBus;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.annotation.APageStyle;
import com.zhpan.bannerview.constants.PageStyle;
import com.zhpan.indicator.enums.IndicatorSlideMode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomeFragment extends BaseFragment{
    RecyclerView recyclerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView recyclerview;
    @BindView(R.id.tvSearchBtn)
    TextView tv_searchLan;
    @BindView(R.id.rlSearchBar)
    RelativeLayout rlSearchBar;
    @BindView(R.id.tablayout1)
    TabLayout TabLayout;
    @BindView(R.id.viewpager_view1)
    ViewPager viewpager;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private int i = 0;
    private ArrayList<Fragment> fragments;
    private BannerViewPager<Integer, ImageResourceViewHolder> mViewPager;
    public List<Integer> mPictureList = new ArrayList<>();

    private Context mContext;

    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;

    private int headHeight;
    private int maxHeadTopHeight,minHeadTopHeight,maxLimit,minLimit,maxMarginBottom,marginLeft;
    private int searchBarHeight,textSize;
    private int searchBarScale,textScale,marginBottomScale;
    private int maxOffset;

    private List<String> tabs;
    private int tabCount = 4;
    TagFragmentAdapter myFragmentPagerAdapter;
    private List<String> data_list;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private Handler handler;
    private int count = 0;
    //private onLoadMoreListener mOnLoadMoreListener;
    /**
     * 默认展开状态
     */
    public static int mStatus = STATUS_EXPANDED;
    public int lastVerticalOffset=1;
    private Boolean loadMore = false;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context.getApplicationContext();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home_constraint;
    }


    @Override
    protected void initView(@org.jetbrains.annotations.Nullable Bundle savedInstanceState, @NotNull View view) {
        ButterKnife.bind(this, view);
        startAnim();

        recyclerView = view.findViewById(R.id.recycleview1);
        tabLayout = view.findViewById(R.id.tablayout1);
        viewPager = view.findViewById(R.id.viewpager_view1);

        mViewPager = view.findViewById(R.id.banner_view);
        mViewPager
                .setIndicatorSlideMode(IndicatorSlideMode.SCALE)
                .setIndicatorSliderColor(getColor(R.color.red_normal_color), getColor(R.color.red_checked_color))
                .setIndicatorSliderRadius(getResources().getDimensionPixelOffset(R.dimen.dp_4), getResources().getDimensionPixelOffset(R.dimen.dp_5))
                .setLifecycleRegistry(getLifecycle())
                .setOnPageClickListener(this::pageClick)
                .setAdapter(new ImageResourceAdapter(getResources().getDimensionPixelOffset(R.dimen.dp_8)))
                .setInterval(3000);
        setupBanner(PageStyle.MULTI_PAGE_OVERLAP);
        setNetEaseMusicStyle();

        preData();
        //initView();
        initData();
        //initListener();
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setEnableLoadMore(false);
        //refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadMore = true;
                orderData();
                startAnim();
                //refreshlayout.finishRefresh(2000);//传入false表示刷新失败

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

            }
        });
    }


    private void preData(){
        tabs = new ArrayList<>();
        tabs.add("购物");
        tabs.add("外卖");
        tabs.add("跑腿");
        tabs.add("闲置");
        orderData();
    }

    private void initView() {

        tabLayout.setupWithViewPager(viewpager);
        myFragmentPagerAdapter = new TagFragmentAdapter(getChildFragmentManager(),fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initData(){

        headHeight = (int) this.getResources().getDimension(R.dimen.headHeight);
        minHeadTopHeight = (int) this.getResources().getDimension(R.dimen.minHeadTopHeight);
        maxHeadTopHeight = (int) this.getResources().getDimension(R.dimen.maxHeadTopHeight);
        maxOffset = maxHeadTopHeight - minHeadTopHeight;
        searchBarHeight = (int) this.getResources().getDimension(R.dimen.searchBarHiight);
        searchBarScale = (int) this.getResources().getDimension(R.dimen.searchBarScale);
        textSize = (int) this.getResources().getDimension(R.dimen.textSize);
        textScale = (int) this.getResources().getDimension(R.dimen.textScale);
        maxMarginBottom = (int) this.getResources().getDimension(R.dimen.maxMarginBottom);
        marginBottomScale = (int) this.getResources().getDimension(R.dimen.marginBottomScale);
        marginLeft = (int) this.getResources().getDimension(R.dimen.marginLeft);
        maxLimit = (int) this.getResources().getDimension(R.dimen.maxLimit);
        minLimit = (int) this.getResources().getDimension(R.dimen.minLimit);
    }


    @OnClick(R.id.rlSearchBar)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSearchBar:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivityForResult(intent,0);
                break;
        }

    }


    private void pageClick(int position) {
        if (position != mViewPager.getCurrentItem()) {
            mViewPager.setCurrentItem(position, true);
        }
        Log.i("TAG", "pageClick: position:" + position);
    }


    private void setupBanner(@APageStyle int pageStyle) {
        mViewPager
                .setPageMargin(getResources().getDimensionPixelOffset(R.dimen.dp_15))
                .setRevealWidth(getResources().getDimensionPixelOffset(R.dimen.dp_10))
                .setPageStyle(pageStyle)
                .create(getPicList(4));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
    }



    // 网易云音乐样式
    private void setNetEaseMusicStyle() {
        mViewPager
                .setPageMargin(getResources().getDimensionPixelOffset(R.dimen.dp_20))
                .setRevealWidth(getResources().getDimensionPixelOffset(R.dimen.dp_m_10))
                .setIndicatorSliderColor(getColor(R.color.red_normal_color), getColor(R.color.red_checked_color))
                .setOnPageClickListener(position -> ToastUtils.show("position:" + position))
                .setInterval(3000).create(getPicList(4));
        mViewPager.removeDefaultPageTransformer();
    }

    private List<productInfoBean> list1 = new ArrayList<>(); //购物
    private List<productInfoBean> list2 = new ArrayList<>(); //外卖
    private List<productInfoBean> list3 = new ArrayList<>(); //跑腿
    private List<productInfoBean> list4 = new ArrayList<>(); //闲置
    private int flag = 0;//查询完成标志
    private void orderData(){
        if (loadMore){
            list1.clear();
            list2.clear();
            list3.clear();
            list4.clear();
        }
        BmobQuery<productInfoBean> query = new BmobQuery<productInfoBean>();
        query.order("-updatedAt");
        query.findObjects(new FindListener<productInfoBean>(){
            @Override
            public void done(List<productInfoBean> list, BmobException e) {
                if (e == null){
                    if (list.size() == 0){
                        ToastUtils.show("没有数据");
                    }else {
                        //数据分类
                        for (int i=0;i<list.size();i++){
                            switch (list.get(i).getOne_category_id()){
                                case 0:
                                    list1.add(list.get(i));
                                    break;
                                case 1:
                                    list2.add(list.get(i));
                                    break;
                                case 2:
                                    list3.add(list.get(i));
                                    break;
                                case 3:
                                    list4.add(list.get(i));
                                    break;
                            }
                        }
                        Log.i("orderData", "done: list1:" + list1.size());
                        Log.i("orderData", "done: list2:" + list2.size());
                        Log.i("orderData", "done: list3:" + list3.size());
                        Log.i("orderData", "done: list4:" + list4.size());

                        if (loadMore){
                            loadMore = false;
                            refreshLayout.finishRefresh();
                            EventMsg eventMsg = new EventMsg();
                            eventMsg.setMsg("1");
                            RxBus.getInstance().post(eventMsg);
                        }
                        stopAnim();
                        initFragment();
                        initView();
                    }
                }else {
                    Log.e("TAG", "查询失败:"+e.getMessage());
                }

            }
        });
    }

    private void initFragment() {
        fragments = new ArrayList<Fragment>();
        for (int i=0;i < 4;i++){
            Bundle data = new Bundle();
            switch (i) {
                case 0:
                    data.putParcelableArrayList("list", (ArrayList<productInfoBean>) list1);
                    break;
                case 1:
                    data.putParcelableArrayList("list", (ArrayList<productInfoBean>) list2);
                    break;
                case 2:
                    data.putParcelableArrayList("list", (ArrayList<productInfoBean>) list3);
                    break;
                case 3:
                    data.putParcelableArrayList("list", (ArrayList<productInfoBean>) list4);
                    break;
            }
            TabFragment tabFragment = new TabFragment();
            tabFragment.setArguments(data);
            fragments.add(tabFragment);

        }
    }

    void startAnim(){
        avi.smoothToShow();
    }

    void stopAnim(){
        avi.smoothToHide();
    }
}
