package com.tita.aixiaoyuan.app.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjq.toast.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tita.aixiaoyuan.Adapter.ShopCarAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.i.OnPriceClickListener;
import com.tita.aixiaoyuan.model.OrderCarBean;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.ui.PayActivity;
import com.tita.aixiaoyuan.ui.ShopDetialActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShopCarFragment extends BaseFragmentJava implements OnPriceClickListener {


    public ShopCarAdapter cartAdapter;
    List<OrderCarBean> orderCarBean;
    double price;
    int num = 0; //选择商品数量
    @BindView(R.id.rv_nest_demo)
    RecyclerView rvNestDemo;
    @BindView(R.id.cart_num)
    TextView cartNum;
    @BindView(R.id.cart_money)
    TextView cartMoney;
    @BindView(R.id.cart_shopp_moular)
    Button cartShoppMoular;
    @BindView(R.id.cbx_quanx_check)
    CheckBox checkBox;
    @BindView(R.id.btn_delete)
    TextView btnDelete;
    @BindView(R.id.SmartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    private String mObjID;
    OrderCarBean.ItemsBean itemsBean;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_shop_car;
    }

    @Override
    protected void setUpView() {
        avi.show();
        //initView();
    }

    @Override
    protected void setUpData() {
        orderCarBean = new ArrayList<>();
        deleteList = new ArrayList<>();
        getCarData(STATE_FLOAD);
    }

    private void initView(){
        rvNestDemo.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new ShopCarAdapter(getContext(),orderCarBean,this);
        rvNestDemo.setAdapter(cartAdapter);
        cartAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i("TAG", "onItemClick: " + position);
                mObjID = cartAdapter.getItem(position).getProduct_objectId();
                Intent intent = new Intent(getContext(), ShopDetialActivity.class);
                intent.putExtra("GoodsObjectId",mObjID);
                startActivityForResult(intent,0);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCarData(STATE_REFRESH);
            }

        });
        cartAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // ToastUtils.show(position);
                ToastUtils.show(view.getId()+":被点击了: "+position);
                Map<Integer, Boolean> isCheck = cartAdapter.getMap();
                int select = 0;
                for (int i=0;i<isCheck.size();i++)
                {
                    if (isCheck.get(i)) select++;
                }
                cartNum.setText("共"+select+"件商品");
                Log.i("TAG", "getPrice: " + cartAdapter.getPriceToActivity());

            }
        });


    }
    @OnClick({R.id.cbx_quanx_check,R.id.btn_delete,R.id.cart_shopp_moular})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.cbx_quanx_check:
                if (cartAdapter != null){
                    checkBox.setClickable(true);
                    // 全选——全不选
                    Map<Integer, Boolean> isCheck = cartAdapter.getMap();
                    if (checkBox.isChecked() == true) {
                        cartAdapter.initCheck(true);
                        // 通知刷新适配器
                        cartAdapter.notifyDataSetChanged();
                    } else if (checkBox.isChecked() == false) {
                        cartAdapter.initCheck(false);
                        // 通知刷新适配器
                        cartAdapter.notifyDataSetChanged();
                    }
                    int select = 0;
                    for (int i=0;i<isCheck.size();i++)
                    {
                        if (isCheck.get(i)) select++;
                    }
                    cartNum.setText("共"+select+"件商品");
                }else {
                    checkBox.setClickable(false);
                }

                break;
            case R.id.btn_delete:
                if (cartAdapter != null){
                    // 拿到全部数据
                    Map<Integer, Boolean> isCheck_delete = cartAdapter.getMap();
                    // 获取到条目数量。map.size = list.size,所以
                    int count = cartAdapter.getItemCount();
                    deleteList.clear();
                    // 遍历
                    for (int i = 0; i < count; i++) {
                        // 删除有两个map和list都要删除 ,计算方式
                        int position = i - (count - cartAdapter.getItemCount());
                        // 推断状态 true为删除
                        if (isCheck_delete.get(i) != null && isCheck_delete.get(i)) {
                            // listview删除数据
                            deleteList.add(cartAdapter.getItem(i).getObjectId());
                            isCheck_delete.remove(i);
                            checkBox.setChecked(false);
                        }
                    }
                    if (deleteList.size()>0){
                        deleteCarGoods();
                    }else {
                        ToastUtils.show("您还未勾选商品");
                    }
                    cartAdapter.notifyDataSetChanged();
                }

                break;
            case R.id.cart_shopp_moular:
                if (cartAdapter != null){
                    List<OrderCarBean> orderList = new ArrayList<>();
                    Map<Integer, Boolean> isCheck = cartAdapter.getMap();
                    int count = cartAdapter.getItemCount();
                    deleteList.clear();
                    for (int i = 0; i < count; i++) {
                        // 删除有两个map和list都要删除 ,计算方式
                        int position = i - (count - cartAdapter.getItemCount());
                        // 推断状态 true为删除
                        if (isCheck.get(i) != null && isCheck.get(i)) {
                            // listview删除数据
                            deleteList.add(cartAdapter.getItem(i).getObjectId());
                            orderList.add(cartAdapter.getItem(i));
                        /*Log.i("PayActivity", "onViewClicked: ---------->"+orderList.get(i).getObjectId());
                        Log.i("PayActivity", "onViewClicked: ---------->"+orderList.get(i).getProduct_Price());
                        Log.i("PayActivity", "onViewClicked: ---------->"+orderList.get(i).getProduct_amount());*/
                            isCheck.remove(i);
                            checkBox.setChecked(false);
                        }
                    }
                    if (orderList.size()>0){
                        Intent intent = new Intent(getContext(), PayActivity.class);
                        intent.putExtra("list", (Serializable) deleteList);
                        intent.putExtra("bundle",(Serializable) orderList);
                        startActivity(intent);
                    }else {
                        ToastUtils.show("还未选择商品!");
                    }
                }


                break;

        }

    }

    private final int STATE_FLOAD = 2;  //第一次加载
    private final int STATE_REFRESH = 1; //下拉刷新状态
    private final int STATE_NONE = 0;
    private String carGoodsObjectId;
    private List<String> deleteList;
    private void getCarData(int state){
        if (BmobUser.isLogin()) {
            BmobUser user = BmobUser.getCurrentUser(User.class);
            String username = user.getUsername();
            String bql ="select * from OrderCarBean where username ='"+username+"'";
            BmobQuery<OrderCarBean> query = new BmobQuery<OrderCarBean>();
            query.setSQL(bql);
            query.order("-updatedAt");
            query.doSQLQuery(new SQLQueryListener<OrderCarBean>() {
                @Override
                public void done(BmobQueryResult<OrderCarBean> result, BmobException e) {
                    orderCarBean.clear();
                    if (state == STATE_REFRESH) {
                        smartRefreshLayout.finishRefresh();
                    }else {
                        avi.hide();
                    }
                    if (e == null){
                        List<OrderCarBean> list = (List<OrderCarBean>) result.getResults();
                        if(list != null && list.size()>0){
                            Log.i("doSearch", "list.size: "+ list.size());
                            orderCarBean.addAll(list);
                            /*if (state == STATE_FLOAD)*/ initView();
                            cartAdapter.setNewData(list);
                        }else {
                            cartAdapter.setNewData(list);
                            ToastUtils.show("没有数据");
                        }
                        cartAdapter.notifyDataSetChanged();

                    }else{
                        Log.i("TAG", "done: " + e.getMessage());
                    }
                }
            });
        }
    }
    private int count = 0;
    private void  deleteCarGoods(){

        // TODO: 2021/3/2 RxJava 批量删除数据
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
/*                for (int i=0;i<deleteList.size();i++){
                    delete(deleteList.get(i));
                    emitter.onNext(deleteList.get(i));
                }*/
                for (int i=0;i<deleteList.size();i++){
                    OrderCarBean orderCarBean = new OrderCarBean();
                    orderCarBean.delete(deleteList.get(i), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            emitter.onNext("1");
                            if (e == null){
                                Log.i("TAG", "done:删除成功 " );
                            }else {
                                Log.e("TAG", "删除失败: " + e.getMessage());
                            }
                        }
                    });

                }
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("Observable", "onNext: "+ s );
                        count++;
                        if (count == deleteList.size()){
                            getCarData(STATE_NONE);
                            cartAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        Log.i("TAG", "onComplete: ");
                    }
                });



    }
    private void delete(String obj){
        OrderCarBean orderCarBean = new OrderCarBean();
        orderCarBean.delete(obj, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Log.i("TAG", "done:删除成功 " );
                }else {
                    Log.e("TAG", "删除失败: " + e.getMessage());
                }
            }
        });
    }
    void startAnim(){
        avi.smoothToShow();
    }

    void stopAnim(){
        avi.smoothToHide();
    }

    @Override
    public void onChangeData() {
        //cartMoney.setText(String.format("%.3f",cartAdapter.getPriceToActivity()));
        cartMoney.setText("" + cartAdapter.getPriceToActivity());
    }
}
