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
import com.tita.aixiaoyuan.Adapter.SearchNestedScrollingFragmentAdapter;
import com.tita.aixiaoyuan.Adapter.TagFragmentAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.viewHolder.ImageResourceViewHolder;
import com.tita.aixiaoyuan.app.widget.XRefreshLayout;
import com.tita.aixiaoyuan.ui.SearchActivity;
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

public class HomeFragment extends BaseFragment {
    RecyclerView recyclerView;
    TabLayout tabLayout;
    ViewPager viewPager;
/*    @BindView(R.id.searchLan)
    TextView tv_searchLan;*/
    RecyclerView recyclerview;
    @BindView(R.id.tvSearchBtn)
    TextView tv_searchLan;
   /* @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;*/
   /* @BindView(R.id.rlSearchBarCover)
    RelativeLayout rlSearchBarCover;*/
    @BindView(R.id.rlSearchBar)
    RelativeLayout rlSearchBar;
   /* @BindView(R.id.tvSearchContentCover)
    TextView tvSearchContentCover;*/
   /* @BindView(R.id.tvSearchBtnCover)
    TextView tvSearchBtnCover;*/
   /* @BindView(R.id.rlSearchBarCoverLayout)
    RelativeLayout rlSearchBarCoverLayout;*/
    /*@BindView(R.id.refreshview)
    XRefreshView refreshview;*/
   /* @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;*/
    @BindView(R.id.tablayout1)
    TabLayout TabLayout;
    @BindView(R.id.viewpager_view1)
    ViewPager viewpager;
    /*@BindView(R.id.coordinatorLayout)
    MyCoordinatorLayout coordinatorLayout;*/
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int i = 0;
    private ArrayList<Fragment> fragments;
    private SearchNestedScrollingFragmentAdapter fragmentAdapter;
    private BannerViewPager<Integer, ImageResourceViewHolder> mViewPager;
    public List<Integer> mPictureList = new ArrayList<>();

    private Context mContext;

    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;
    private XRefreshLayout xRefreshLayout;
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
        //ButterKnife.bind(R.id.recycler_view, view);

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
        initView();
        initData();
        //initListener();
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        //refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000);//传入false表示加载失败
            }
        });



    }


    private void preData(){
        tabs = new ArrayList<>();
        tabs.add("购物");
        tabs.add("外卖");
        tabs.add("跑腿");
        tabs.add("闲置");
        fragments = new ArrayList<Fragment>();
        /*for(int i=0;i<tabCount;i++){
           // SearchNestedScrollingBaseFragment fragment = new SearchNestedScrollingBaseFragment();
            fragment.setIndex(i);
            fragments.add(fragment);
        }*/
        fragments.add(new TabFragment());
        fragments.add(new TabFragment());
        fragments.add(new TabFragment());
        fragments.add(new TabFragment());
    }

    private void initView() {

       // fragmentAdapter = new SearchNestedScrollingFragmentAdapter(getChildFragmentManager(),fragments);
       // viewpager.setAdapter(fragmentAdapter);
        //viewPager.setOffscreenPageLimit(1);
        //coordinatorLayout.setCurrentScrollableContainer(fragments.get(0));
        //smartTabLayout.setCustomTabView(new MyTabProvider());
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

  //  private TagFragmentAdapter tapFragmentAdapter;

    /*public void initView(){
            tabFragmentList.add(new ShopFragment());
            tabFragmentList.add(new FoodFragment());
            tabFragmentList.add(new RunFragment());
            tabFragmentList.add(new IdelFragment());
            // 2.适配器
           TagFragmentAdapter tapFragmentAdapter = new TagFragmentAdapter(getChildFragmentManager(),tabFragmentList){
                @Override
                public void startUpdate(@NonNull ViewGroup container) {
                    super.startUpdate(container);
                }
            };
            // 3.适配器 交给 ViewPager
            viewPager.setAdapter(tapFragmentAdapter);
            //设置TabLayout和ViewPager联动
            tabLayout.setupWithViewPager(viewPager,false);
            viewPager.setOffscreenPageLimit(4);

        xRefreshLayout = new XRefreshLayout(getContext());
        refreshview.setCustomHeaderView(xRefreshLayout);
        refreshview.setPinnedContent(true);

        }*/


/*    private void initListener(){
        refreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener(){
            @Override
            public void onRefresh() {
                super.onRefresh();
                refreshview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshview.stopRefresh();
                    }
                },1000);
            }

            @Override
            public void onHeaderMove(double offset, int offsetY) {
                super.onHeaderMove(offset, offsetY);
                //offset:移动距离和headerview高度的比例。范围是0~1，0：headerview全然没显示 1：headerview全然显示
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
            }


            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
            }

        });
        */

     /*   viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                //coordinatorLayout.setCurrentScrollableContainer(fragments.get(position));
            }
        });*/

      /*  coordinatorLayout.setAppBarLayoutObserved(this::getLayout);
        coordinatorLayout.setxRefreshView(refreshview);
        coordinatorLayout.setNestedScrollView(nestedScrollView);*/


   // }

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


    private List<String> getData() {
        List<String> data = new ArrayList<>();
        for (int tempI = i; i < tempI + 10; i++) {
            data.add("ChildView item " + i);
        }
        return data;
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


   /* private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_CONTENT = 0;//正常内容
        private final static int TYPE_FOOTER = 1;//加载View

        @Override
        public int getItemViewType(int position) {
            if (position == data_list.size()) {
                return TYPE_FOOTER;
            }
            return TYPE_CONTENT;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_foot, parent, false);
                return new FootViewHolder(view);
            } else {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                MyViewHolder myViewHolder = new MyViewHolder(view);
                return myViewHolder;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
            } else {
                MyViewHolder viewHolder = (MyViewHolder) holder;
                viewHolder.textView.setText("第" + position + "行");
            }
        }


        @Override
        public int getItemCount() {
            return data_list.size() + 1;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_index);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        ContentLoadingProgressBar contentLoadingProgressBar;

        public FootViewHolder(View itemView) {
            super(itemView);
            contentLoadingProgressBar = itemView.findViewById(R.id.pb_progress);
        }
    }*/
}
