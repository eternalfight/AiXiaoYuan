package com.tita.aixiaoyuan.app.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tita.aixiaoyuan.Adapter.ListAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.ui.MainActivity;
import com.tita.aixiaoyuan.ui.ShopDetialActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TabFragment extends BaseFragmentJava {
   /* private static TabFragment instance;

    private TabFragment(){};

    public synchronized static TabFragment getInstance(){
        if (instance == null){
            instance = new TabFragment();
        }
        return instance;
    }
*/

    /*public static TabFragment newInstance(String label) {
        Bundle args = new Bundle();
        args.putString("label", label);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }*/
    @BindView(R.id.recycleview1)
    RecyclerView recyclerview;
    @BindView(R.id.smartrexfresh)
    SmartRefreshLayout smartrexfresh;
    @BindView(R.id.recycleview_footer)
    ClassicsFooter recycleview_footer;
    private ListAdapter adapter;
    private MainActivity mActivity;
    private int index;
    private GridLayoutManager gridLayoutManager;
    List<String> listData;
    String TAG = "TabFragment";
    int count = 0;
    private static  SearchNestedScrollingBaseFragment instance;
    public synchronized static  SearchNestedScrollingBaseFragment getInstance(){
        if (instance == null){
            instance = new SearchNestedScrollingBaseFragment();
        }
        return instance;
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void setUpView() {
        initView();
    }

    @Override
    protected void setUpData() {

    }

    private void initView() {
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new ListAdapter(getContext(),this.getListData((index+1)*10));
        recyclerview.setAdapter(adapter);
        smartrexfresh.setRefreshFooter(new ClassicsFooter(getContext()));;
        recycleview_footer.setFinishDuration(0);

        smartrexfresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData("refresh");
                refreshlayout.finishRefresh(1000);//传入false表示刷新失败
            }
        });
        smartrexfresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getData("loadMore");
                refreshlayout.finishLoadMore(1000);//传入false表示加载失败
            }
        });

        adapter.setItemClickListener(new ListAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(View view, int position) {
                Log.d(TAG,"root clicked..." + position);
                Intent intent = new Intent(getContext(), ShopDetialActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }



    public void setIndex(int index){
        this.index = index;
    }

    public List<String> getListData(int count){
        listData = new ArrayList<>();
        for(int i=0;i<count;i++){
            listData.add("Item-"+i);
        }
        return listData;
    }
    private void getData(final String type) {
        if ("reset".equals(type)) {
            listData.clear();
            count = 0;
            for (int i = 0; i < 10; i++) {
                count += 1;
                listData.add(count+"");
            }
        }
        else if ("refresh".equals(type)) {
            listData.clear();
            count = 0;
            for (int i = 0; i < 14; i++) {
                count += 1;
                listData.add(count+"");
            }
        } else {
            for (int i = 0; i < 6; i++) {
                count += 1;
                listData.add(count+"");
            }
        }

        adapter.notifyDataSetChanged();
       /* if (smartrexfresh.isRefreshing()) {
            smartrexfresh.finishLoadMore(2000);
        }*/
        if ("refresh".equals(type)) {
            //Toast.makeText(getContext(), "刷新完毕", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(getContext(), "加载完毕", Toast.LENGTH_SHORT).show();
        }
    }

}