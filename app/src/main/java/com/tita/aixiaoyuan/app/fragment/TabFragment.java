package com.tita.aixiaoyuan.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Adapter.GoodsAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.EventMsg;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.ui.ShopDetialActivity;
import com.tita.aixiaoyuan.utils.RxBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TabFragment extends BaseFragmentLazyLoad  {

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    @BindView(R.id.recycleview1)
    RecyclerView recyclerview;


    private GoodsAdapter goodsAdapter;

    private GridLayoutManager gridLayoutManager;
    List<productInfoBean> orderListData = new ArrayList<>();
    List<productInfoBean> productInfo = new ArrayList<productInfoBean>();;
    String TAG = "TabFragment";
    private String mObjID;




    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void setUpView() {

        isPrepared = true;
        //实现懒加载
        lazyLoad();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected void setUpData() {

        //queryData(0,STATE_REFRESH);
        //initGoodsDate();
        //initView();
    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        getDataFromFragment();
        mHasLoadedOnce = true;

    }

    private void initView() {
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerview.setLayoutManager(gridLayoutManager);
        goodsAdapter = new GoodsAdapter(getContext(),productInfo);
        recyclerview.setAdapter(goodsAdapter);
        goodsAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        goodsAdapter.isFirstOnly(true);
        goodsAdapter.setNotDoAnimationCount(4);
        goodsAdapter.setPreLoadNumber(6);
        goodsAdapter.setNewData(productInfo);
        goodsAdapter.notifyDataSetChanged();

        goodsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //loadMore();
                //queryData(0,STATE_MORE);
            }
        });

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "onItemClick: " + position);
                mObjID = goodsAdapter.getItem(position).getObjectId();
                Intent intent = new Intent(getContext(),ShopDetialActivity.class);
                intent.putExtra("GoodsObjectId",mObjID);
                startActivityForResult(intent,0);
            }
        });


    }

    public void initGoodsDate(){

        BmobQuery<productInfoBean> query = new BmobQuery<productInfoBean>();
        query.order("-updatedAt");
        query.findObjects(new FindListener<productInfoBean>(){
            @Override
            public void done(List<productInfoBean> list, BmobException e) {

                if (e == null){
                    if (list.size() == 0){
                        ToastUtils.show("没有数据");
                    }else {
                        productInfo = list;
                        goodsAdapter.setNewData(productInfo);
                        goodsAdapter.notifyDataSetChanged();

                    }
                }else {
                    Log.e(TAG, "查询失败:"+e.getMessage());
                }

            }
        });
    }
    private void loadMore() {

        BmobQuery<productInfoBean> query = new BmobQuery<productInfoBean>();
        //query.order("-updatedAt");
        query.findObjects(new FindListener<productInfoBean>(){
            @Override
            public void done(List<productInfoBean> list, BmobException e) {
                goodsAdapter.loadMoreEnd();

                if (e == null){
                    goodsAdapter.loadMoreComplete();
                    if (list.size() == 0){
                        ToastUtils.show("没有数据");

                    }else {
                        productInfo = list;
                        goodsAdapter.addData(productInfo);
                        goodsAdapter.notifyDataSetChanged();
                    }
                }else {
                    Log.e(TAG, "查询失败:"+e.getMessage());
                    goodsAdapter.loadMoreFail();
                }

            }
        });
    }
    private String lastTime = null;
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 50; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始
    /**
     * 分页获取数据
     *
     * @param page
     *            页码
     * @param actionType
     *            ListView的操作类型（下拉刷新、上拉加载更多）
     */
    private void queryData(int page, final int actionType){
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType +" curPage:" + curPage );
        BmobQuery<productInfoBean> query = new BmobQuery<productInfoBean>();
        // 按时间降序查询
        query.order("-createdAt");
        int count = 0;
        // 如果是加载更多
        if (actionType == STATE_MORE){
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                Log.i("0414", date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * count + 1);
        }else {
            // 下拉刷新
            page = 0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<productInfoBean>(){
            @Override
            public void done(List<productInfoBean> list, BmobException e) {

                if (actionType == STATE_MORE) goodsAdapter.loadMoreEnd();
                if (e == null){
                    if (list.size() == 0){
                        ToastUtils.show("没有数据");
                    }else {
                        if (actionType == STATE_REFRESH){
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把productInfo清空，重新添加
                            curPage = 0;
                            productInfo.clear();
                            lastTime = list.get(list.size() - 1).getCreatedAt();

                        }
                            productInfo.clear();
                            productInfo = list;
                            for(int i = 0; i<list.size() ;i++){
                                Log.i("TAG", "done: " + list.get(i).getProduct_name());
                                if (i == list.size()-1) {
                                    Log.i(TAG, "----------------------------------------------");
                                }
                            }

                            goodsAdapter.addData(productInfo);


                        Log.i("bmob", "list.size(): "+  list.size() + ",productInfo :" + productInfo.size() + " ,goodsAdapter:" +goodsAdapter.getData().size());
                        //goodsAdapter.addData(productInfo);
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
                        if (actionType == STATE_MORE) goodsAdapter.loadMoreComplete();
                        goodsAdapter.notifyDataSetChanged();

                    }
                }else {
                    Log.e(TAG, "查询失败:"+e.getMessage());
                    if (actionType == STATE_MORE) goodsAdapter.loadMoreFail();
                }

            }
        });
   }

   private void getDataFromFragment(){
       Bundle args = getArguments();
       if (args != null) {
           productInfo = args.getParcelableArrayList("list");
           Log.i("orderData ", "getDataFromFragment  --->: "+ productInfo.size());
           initView();
       }
       getMsg();
   }





    private void getMsg(){
        Disposable subscribe = RxBus.getInstance().toObservable().map(new Function<Object, EventMsg>() {
            @Override
            public EventMsg apply(Object o) throws Exception {
                return (EventMsg) o;
            }
        }).subscribe(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
                if (eventMsg != null) {
                    //do something
                    Bundle args = getArguments();
                    Log.i("orderData", "accept: " +args.size());
                    if (args != null) {
                        productInfo = args.getParcelableArrayList("list");
                        Log.i("orderData ", "getDataFromFragment  --->: "+ productInfo.size());
                        initView();
                    }
                }
            }
        });
    }

}