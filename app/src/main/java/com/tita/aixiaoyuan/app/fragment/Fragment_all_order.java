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
import com.tita.aixiaoyuan.model.MyOrderBean;
import com.tita.aixiaoyuan.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Fragment_all_order extends BaseFragmentJava {
    @BindView(R.id.orderRecycleView)
    RecyclerView orderRecycleView;
    OrderAdapter mAdapter;
    List<MyOrderBean> myOrderBeans = new ArrayList<>();
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_all_order;
    }

    @Override
    protected void setUpView() {
        initView();
        initData();
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
        mAdapter = new OrderAdapter(myOrderBeans);
        orderRecycleView.setAdapter(mAdapter);
        //mAdapter.setNewData(myOrderBeans);

    }
    public void initData(){
        BmobQuery<MyOrderBean> query = new BmobQuery<>();
        BmobUser user = BmobUser.getCurrentUser(User.class);
        query.addWhereEqualTo("username",user.getUsername());
        query.findObjects(new FindListener<MyOrderBean>() {
            @Override
            public void done(List<MyOrderBean> list, BmobException e) {
                if (e == null){
                    Log.i("TAG", "done: "+list.size());
                    mAdapter.setNewData(list);
                    mAdapter.notifyDataSetChanged();
                }else {
                    Log.e("TAG", "done: " +e.getMessage());
                }
            }
        });

    }



}
