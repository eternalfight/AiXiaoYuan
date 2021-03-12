package com.tita.aixiaoyuan.app.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Adapter.GoodsSearchResultAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.ui.ShopDetialActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class MyPublishFragmemt extends BaseFragmentJava{
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    GoodsSearchResultAdapter goodsSearchResultAdapter;
    List<productInfoBean> productInfo;
    private String mObjID;
    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_mypublish;
    }

    @Override
    protected void setUpView() {
        startAnim();
        initView();
        doSearch();
    }

    @Override
    protected void setUpData() {

    }
    private void initView(){
        goodsSearchResultAdapter = new GoodsSearchResultAdapter(getContext(),productInfo,GoodsSearchResultAdapter.SEALER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(goodsSearchResultAdapter);
        goodsSearchResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i("TAG", "onItemClick: " + position);
                mObjID = goodsSearchResultAdapter.getItem(position).getObjectId();
                Intent intent = new Intent(getContext(), ShopDetialActivity.class);
                intent.putExtra("GoodsObjectId",mObjID);
                startActivityForResult(intent,0);
            }
        });
    }
    private void doSearch(){
        BmobUser user = BmobUser.getCurrentUser(User.class);
        String username = user.getUsername();
        String bql ="select * from productInfoBean where username ='"+username+"'";
        BmobQuery<productInfoBean> query = new BmobQuery<>();
        query.setSQL(bql);
        query.order("-updatedAt");
        query.doSQLQuery(new SQLQueryListener<productInfoBean>() {
            @Override
            public void done(BmobQueryResult<productInfoBean> result, BmobException e) {
                avi.hide();
                if (e == null){
                    Log.i("doSearch", "doSearch: "+ result.getCount());
                    List<productInfoBean> list = (List<productInfoBean>) result.getResults();
                    if(list != null && list.size()>0){
                        Log.i("doSearch", "list.size: "+ list.size());
                        goodsSearchResultAdapter.setNewData(list);
                        goodsSearchResultAdapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.show("没有数据");
                    }

                }else{
                    Log.i("TAG", "done: " + e.getMessage());
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
