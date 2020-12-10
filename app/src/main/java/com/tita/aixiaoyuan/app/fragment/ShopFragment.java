package com.tita.aixiaoyuan.app.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.Adapter.AdapterFragment;
import com.tita.aixiaoyuan.Adapter.WrappingGridLayoutManager;
import com.tita.aixiaoyuan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ShopFragment extends BaseFragmentJava {
    @BindView(R.id.recycleview1)
    RecyclerView recyclerView;
    private List<String> strDatas = new ArrayList<>();
    private AdapterFragment adapterFragment;

    public void initViews(){
        adapterFragment = new AdapterFragment(getContext(), strDatas);
        recyclerView.setAdapter(adapterFragment);
        WrappingGridLayoutManager gridLayoutManager = new WrappingGridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);//固定自身size不受adapter变化影响
        //消除滑动卡顿现象 ...
        recyclerView.setNestedScrollingEnabled(false);//限制recyclerview自身滑动特性,滑动全部靠scrollview完成
        initData();
    }
    private void initData() {
        for (int i = 0; i < 10; i++) {
            strDatas.add("item  " + i);
        }
        adapterFragment.notifyDataSetChanged();
    }


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void setUpView() {
        initViews();
    }

    @Override
    protected void setUpData() {

    }
}
