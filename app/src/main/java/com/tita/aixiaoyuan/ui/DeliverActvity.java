package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tita.aixiaoyuan.Adapter.DeliverAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.DeliverBean;
import com.tita.aixiaoyuan.model.MyOrderBean;
import com.tita.aixiaoyuan.model.User;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeliverActvity extends AppCompatActivity {
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    SweetAlertDialog pDialog;
    private String mObjID;
    private DeliverBean deliverBean;
    private List<DeliverBean> deliverBeansList;
    DeliverAdapter deliverAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_actvity);
        ButterKnife.bind(this);
        startAnim();
        initData();
        initView();
    }

    private void initData(){
        deliverBeansList = new ArrayList<>();
        Intent intent = getIntent();
        mObjID = intent.getStringExtra("GoodsObjectId");
        if (mObjID!=null){
            Log.i("equal", "initData mObjID:"+mObjID);
            equal();
        }
    }

    private void initView(){
        deliverAdapter = new DeliverAdapter(deliverBeansList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(deliverAdapter);
        deliverAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pDialog = new SweetAlertDialog(DeliverActvity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("正在处理");
                pDialog.setCancelable(false);
                pDialog.show();
                MyOrderBean myOrderBean= new MyOrderBean();
                myOrderBean.setOrder_status(2);
                myOrderBean.update(deliverAdapter.getItem(position).getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        pDialog.dismiss();
                        if (e == null){
                            deliverAdapter.remove(position);
                            deliverAdapter.notifyItemRemoved(position);
                        }else {
                            Log.e("DeliverActvity", "done: "+e.getMessage() );
                        }
                    }
                });
            }
        });
    }
    private void equal() {
        BmobQuery<MyOrderBean> categoryBmobQuery = new BmobQuery<>();
        BmobUser user = BmobUser.getCurrentUser(User.class);
        categoryBmobQuery.order("-updatedAt");
        categoryBmobQuery.findObjects(new FindListener<MyOrderBean>() {
            @Override
            public void done(List<MyOrderBean> object, BmobException e) {
                deliverBean = new DeliverBean();
                if (e == null) {
                    Log.i("equal", "done: "+object.size());
                    if (object.size()==0) stopAnim();
                    for (MyOrderBean o : object){
                        int i = 0;

                        for (String s : o.getProduct_id()){
                            Log.i("equal", "Product_id: "+s);
                            if (s.equals(mObjID) && o.getOrder_status()==1){
                                deliverBean.setUsername(o.getUsername());
                                deliverBean.setNum(o.getProduct_cnt().get(i));
                                deliverBean.setOrderID(o.getOrderId());

                                BmobQuery<User> query = new BmobQuery<>();
                                Log.i("equal", "username: "+o.getUsername());
                                query.addWhereEqualTo("username", o.getUsername());
                                query.findObjects(new FindListener<User>() {
                                    @Override
                                    public void done(List<User> list, BmobException e) {
                                        stopAnim();
                                        Log.i("equal", "done: "+list.size());
                                        if (e==null){
                                            if (list.size()>0){
                                                deliverBean.setPhone(list.get(0).getMobilePhoneNumber());
                                                deliverBean.setAddr(list.get(0).getAddress());
                                                deliverBean.setObjectId(o.getObjectId());
                                                Log.i("equal", "setAddr: "+list.get(0).getAddress());
                                                Log.i("equal", "getAddr: "+deliverBean.getAddr());
                                                deliverAdapter.addData(deliverBean);
                                                deliverAdapter.notifyDataSetChanged();
                                            }

                                        }else {
                                            Log.e("equal", e.toString());
                                        }
                                    }
                                });
                                i++;
                            }
                        }
                    }

                } else {
                    Log.e("equal", e.toString());
                }
            }
        });



    }

    void startAnim(){
        // avi.show();
        avi.smoothToShow();
    }

    void stopAnim(){
        //avi.hide();
        avi.smoothToHide();
    }
}