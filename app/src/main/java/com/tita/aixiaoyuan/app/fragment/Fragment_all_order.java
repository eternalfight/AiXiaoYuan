package com.tita.aixiaoyuan.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.Adapter.OrderAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.OrderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Fragment_all_order extends BaseFragmentJava {
    @BindView(R.id.orderRecycleView)
    RecyclerView orderRecycleView;
    OrderAdapter orderAdapter;
    private OrderBean.DataBean dataBean;
    private OrderBean orderBean;
    private List<OrderBean.DataBean> data;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_all_order;
    }

    @Override
    protected void setUpView() {


        initData();
        initView();
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecycleView.setLayoutManager(manager);
        Log.i("data", "initView: "+orderBean.getData());
        orderAdapter = new OrderAdapter(getContext(),orderBean.getData());
        orderRecycleView.setAdapter(orderAdapter);
    }
    public void initData(){
        //orderBean = new OrderBean("1","1","0",data);
        //OrderBean.DataBean dataBean1 = new OrderBean.DataBean("2020-10-22",115465464,111,1,"显卡",16);
        dataBean = new OrderBean.DataBean();
        dataBean.setCreatetime("2020-10-21");
        dataBean.setOrderid(115465464);
        dataBean.setPrice(12);
        dataBean.setStatus(1);
        dataBean.setTitle("显卡");
        dataBean.setUid(15);
        data = new ArrayList<>();
        //data.add(dataBean1);
        //dataBean = new OrderBean.DataBean("2020-10-21",115465464,12,1,"显卡",15);
        data.add(dataBean);
        orderBean = new OrderBean("1","1","0",data);


    }


}
