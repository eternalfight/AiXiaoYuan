package com.tita.aixiaoyuan.app.fragment;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Adapter.OrderAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.MyOrderBean;
import com.tita.aixiaoyuan.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class Fragment_wait_for_recive extends BaseFragmentJava {
    @BindView(R.id.orderRecycleView)
    RecyclerView orderRecycleView;
    OrderAdapter mAdapter;
    List<MyOrderBean> myOrderBeans = new ArrayList<>();

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_wait_for_recever;
    }

    @Override
    protected void setUpView() {
        initView();
        doSearch();
    }

    @Override
    protected void setUpData() {

    }
    public void initView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecycleView.setLayoutManager(manager);
        mAdapter = new OrderAdapter(myOrderBeans);
        orderRecycleView.setAdapter(mAdapter);
        //mAdapter.setNewData(myOrderBeans);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void doSearch(){
        BmobUser user = BmobUser.getCurrentUser(User.class);
        String username = user.getUsername();
        String bql ="select * from MyOrderBean where username ='"+username+"' and order_status ="+2;
        BmobQuery<MyOrderBean> query = new BmobQuery<>();
        query.setSQL(bql);
        query.order("-updatedAt");
        query.doSQLQuery(new SQLQueryListener<MyOrderBean>() {
            @Override
            public void done(BmobQueryResult<MyOrderBean> result, BmobException e) {
                if (e == null){
                    Log.i("doSearch", "doSearch: "+ result.getCount());
                    List<MyOrderBean> list = (List<MyOrderBean>) result.getResults();
                    if(list != null && list.size()>0){
                        Log.i("doSearch", "list.size: "+ list.size());
                        mAdapter.setNewData(list);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.show("没有数据");
                    }

                }else{
                    Log.i("TAG", "done: " + e.getMessage());
                }
            }
        });
    }
}
