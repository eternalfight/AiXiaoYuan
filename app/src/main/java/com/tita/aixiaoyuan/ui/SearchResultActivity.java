package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Adapter.GoodsSearchResultAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class SearchResultActivity extends AppCompatActivity {

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    @BindView(R.id.search_header_tv)
    EditText searchInput_et;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    GoodsSearchResultAdapter goodsSearchResultAdapter;
    List<productInfoBean> productInfo;
    private String searchInput;
    private String mObjID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        getInputData();
        initView();
        avi.show();
        doSearch();
    }

    private void initView(){
        goodsSearchResultAdapter = new GoodsSearchResultAdapter(this,productInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(goodsSearchResultAdapter);
        goodsSearchResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i("TAG", "onItemClick: " + position);
                mObjID = goodsSearchResultAdapter.getItem(position).getObjectId();
                Intent intent = new Intent(SearchResultActivity.this,ShopDetialActivity.class);
                intent.putExtra("GoodsObjectId",mObjID);
                startActivityForResult(intent,0);
            }
        });
    }

    private void doSearch(){
        //String bql ="select * from productInfoBean where product_name like'%"+searchInput+"%'";
        String bql ="select * from productInfoBean where product_name ='"+searchInput+"'";
        BmobQuery<productInfoBean> query = new BmobQuery<>();
        query.setSQL(bql);
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
                                         ToastUtils.show("没有该商品");
                                     }

                                 }else{
                                     Log.i("TAG", "done: " + e.getMessage());
                                 }
                             }
                         });


        /*query.addWhereContains("product_name",searchInput);
        query.order("-createdAt");
        Log.i("TAG", "doSearch: "+ searchInput);
        query.findObjects(new FindListener<productInfoBean>() {
            @Override
            public void done(List<productInfoBean> list, BmobException e) {
                if (e == null){
                    Log.i("TAG", "doSearch: "+ list.size());
                    if(list.size()>0){

                        goodsSearchResultAdapter.setNewData(list);
                        goodsSearchResultAdapter.notifyDataSetChanged();
                    }else {
                        ToastUtils.show("没有该商品");
                    }

                }else{
                    Log.i("TAG", "done: " + e.getMessage());
                }

            }
        });*/
    }
    private void getInputData(){
        Intent intent = getIntent();
        searchInput = intent.getStringExtra("input");
    }
    @OnClick(R.id.header_back_image)
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.header_back_image:
                finish();
                break;

        }
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