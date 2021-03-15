package com.tita.aixiaoyuan.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.tita.aixiaoyuan.Adapter.OrderConfirmAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.EventMsg;
import com.tita.aixiaoyuan.model.MyOrderBean;
import com.tita.aixiaoyuan.model.OrderCarBean;
import com.tita.aixiaoyuan.model.User;
import com.tita.aixiaoyuan.model.productInfoBean;
import com.tita.aixiaoyuan.utils.RxBus;
import com.tita.aixiaoyuan.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class PayActivity extends AppCompatActivity {

    @BindView(R.id.pay_btn)
    Button pay;
    @BindView(R.id.all_money)
    TextView all_money;
    @BindView(R.id.cart_num)
    TextView num;
    @BindView(R.id.RecyclerView)
    RecyclerView mRecyclerView;
    private final String TAG = "PayActivity" ;
    private String price = "";
    List<Integer>  stockList = new ArrayList<>(); //库存表
    private OrderConfirmAdapter adapter;
    private List<OrderCarBean> getOrder;
    private String orderNum;
    private List<String> goodsIdList = new ArrayList<>();
    private List<String> goodsIdPrice = new ArrayList<>();
    private List<Integer> product_cnt =new ArrayList<>();
    private List<String> product_name =new ArrayList<>();
    private String money;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initData();
        initView();
        getAdapterMsg();
    }
    private void initView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderConfirmAdapter(getOrder);
        mRecyclerView.setAdapter(adapter);
        //adapter.setNewData(getOrder);
        adapter.addHeaderView(getHeaderView(),0);
       // adapter.notifyDataSetChanged();


    }
    private void initListener(){

    }

    @OnClick({R.id.sale_cancle_btn,R.id.pay_btn})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.pay_btn:
                doBuy();
                break;
            case R.id.sale_cancle_btn:
                finish();
                break;
        }
    }

    private void initData(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("正在生成订单");
        pDialog.setCancelable(false);
        pDialog.show();

        orderNum = Utils.genUniqueKey();
        getOrder = new ArrayList<>();
        getOrder = (List<OrderCarBean>) getIntent().getSerializableExtra("bundle");
        //Log.i(TAG, "accept: orderCarBean ----------> " +getOrder.size()+";"+ getOrder.get(0).getProduct_name());
        for (OrderCarBean b: getOrder){
            Log.i(TAG, "initData: " + b.getObjectId());
            Log.i(TAG, "initData: " + b.getProduct_Price());
            Log.i(TAG, "initData: " + b.getProduct_amount());
            goodsIdList.add(b.getProduct_objectId());
            goodsIdPrice.add(b.getProduct_Price());
            product_cnt.add(b.getProduct_amount());
            product_name.add(b.getProduct_name());
            query(b.getProduct_objectId());

        }



    }
    private int i = 0;
    private void query(String mObjectId) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                BmobQuery<productInfoBean> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject(mObjectId, new QueryListener<productInfoBean>() {
                    @Override
                    public void done(productInfoBean productInfo, BmobException e) {
                        if (e == null) {
                            stockList.add(productInfo.getCurrent_cnt());
                            i++;
                            emitter.onNext(1);
                            Log.i(TAG, "done: "+ productInfo.getCurrent_cnt());
                        } else {
                            Log.e("BMOB", e.toString());
                            emitter.onComplete();
                        }
                    }
                });
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }
            @Override
            public void onNext(@NonNull Integer integer) {
                Log.i(TAG, "onNext: " +i);
                Log.i(TAG, "getOrder.size(): " +getOrder.size());
                if (i == getOrder.size()){
                    int j =0;
                    for (OrderCarBean b: getOrder){
                        int amout = b.getProduct_amount();
                        if (amout > stockList.get(j)) {
                            pay.setClickable(false);
                            //pay.setBackgroundColor(192);
                            pay.setBackgroundResource(R.color.gray_btn_bg_pressed_color);
                            ToastUtils.show("订单中有无货商品");
                        }
                        j++;
                    }
                    pDialog.dismiss();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                pDialog.dismiss();
            }

            @Override
            public void onComplete() {
                pDialog.dismiss();
            }
        });


    }
    private void getAdapterMsg(){
        Disposable subscribe = RxBus.getInstance().toObservable().map(new Function<Object, EventMsg>() {
            @Override
            public EventMsg apply(Object o) throws Exception {
                return (EventMsg) o;
            }
        }).subscribe(new Consumer<EventMsg>() {
            @Override
            public void accept(EventMsg eventMsg) throws Exception {
                if (eventMsg != null) {
                    all_money.setText(eventMsg.getMsg());
                    money = eventMsg.getMsg();
                }
            }
        });
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.order_head, mRecyclerView, false);
        TextView  tv_orderNum = view.findViewById(R.id.order_head_shop_name);
        tv_orderNum.setText("订单号：" + orderNum);
        return view;
    }
    SweetAlertDialog pDialog1;
    private void doBuy(){

        pDialog1 = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog1.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog1.setTitleText("购买成功！");
        pDialog1.setCancelable(false);

        pDialog.show();
        MyOrderBean myOrderBean = new MyOrderBean();
        BmobUser user = BmobUser.getCurrentUser(User.class);
        myOrderBean.setUsername(user.getUsername());
        myOrderBean.setOrder_status(1);
        myOrderBean.setProduct_id(goodsIdList);
        myOrderBean.setProduct_cnt(product_cnt);
        myOrderBean.setProduct_price(goodsIdPrice);
        myOrderBean.setOrderId(orderNum);
        myOrderBean.setPrice(money);
        myOrderBean.setProduct_name(product_name);
        myOrderBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    doDelete();
                    pDialog.dismiss();
                }else{
                    Log.i(TAG, "创建数据失败: " + e.getMessage());
                }
            }
        });
    }
    int flag = 0;
    private void doDelete(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i =0;i<getOrder.size();i++){
                    delete(getOrder.get(i).getObjectId());
                    updateProductInfo(getOrder.get(i).getProduct_objectId(),getOrder.get(i).getProduct_amount());
                    flag++;
                    emitter.onNext(1);
                }
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.i(TAG, "onNext flag: " +flag+"getOrder.size():"+getOrder.size());

                if (flag == getOrder.size()){
                    pDialog1.show();
                    pDialog1.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    }).show();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void updateProductInfo(String obj,int num){
        productInfoBean productInfoBean =new productInfoBean();
        BmobQuery<productInfoBean> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(obj, new QueryListener<productInfoBean>() {
            @Override
            public void done(productInfoBean product, BmobException e) {
                if (e == null) {
                    productInfoBean.setCurrent_cnt(product.getCurrent_cnt()-num);
                    if (product.getCurrent_cnt()-num ==0){
                        productInfoBean.setSellout(1);
                    }
                    productInfoBean.setPublish_status(2);
                    productInfoBean.update(obj,new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                Log.i("TAG", "done:更新成功 " );
                            }else {
                                Log.e("TAG", "更新失败: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.e("TAG", "更新失败: " + e.getMessage());
                }
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
                    pDialog1.dismiss();
                    pDialog.dismiss();
                }
            }
        });
    }

}