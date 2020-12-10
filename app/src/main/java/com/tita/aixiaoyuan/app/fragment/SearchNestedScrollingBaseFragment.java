package com.tita.aixiaoyuan.app.fragment;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.tita.aixiaoyuan.Adapter.ListAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.i.ScrollableContainer;
import com.tita.aixiaoyuan.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchNestedScrollingBaseFragment extends BaseFragmentJava implements ScrollableContainer {
    @BindView(R.id.recycleview1)
    RecyclerView recyclerview;
    @BindView(R.id.smartrexfresh)
    SmartRefreshLayout smartrexfresh;
    private ListAdapter adapter;
    private MainActivity mActivity;
    private int index;
    private GridLayoutManager gridLayoutManager;
    List<String> listData;
    int count = 0;
    private static  SearchNestedScrollingBaseFragment instance;
    public synchronized static  SearchNestedScrollingBaseFragment getInstance(){
        if (instance == null){
            instance = new SearchNestedScrollingBaseFragment();
        }
        return instance;
    }

    @Override
    public View getScrollableView() {
        return recyclerview;
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
        smartrexfresh.setRefreshFooter(new ClassicsFooter(getContext())).setEnableRefresh(false);
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
            for (int i = 0; i < 13; i++) {
                count += 1;
                listData.add(count+"");
            }
        } else {
            for (int i = 0; i < 3; i++) {
                count += 1;
                listData.add(count+"");
            }
        }

        /*adapter.notifyDataSetChanged();
        if (smartrexfresh.isRefreshing()) {
            smartrexfresh.setRefreshing(false);
        }
        if ("refresh".equals(type)) {
            //Toast.makeText(getContext(), "刷新完毕", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(getContext(), "加载完毕", Toast.LENGTH_SHORT).show();
        }*/
    }
}
