package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.toast.ToastUtils;
import com.luck.picture.lib.tools.ValueOf;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.i.OnPriceClickListener;
import com.tita.aixiaoyuan.model.OrderCarBean;
import com.tita.aixiaoyuan.model.productInfoBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShopCarAdapter extends BaseQuickAdapter<OrderCarBean, BaseViewHolder> {
    private Context context;
    private OnPriceClickListener onPriceClickListener;
    private List<OrderCarBean> data;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();
    // 初始化map集合
    public void initCheck(boolean flag) {
        // map集合的数量和list的数量是一致的
        for (int i = 0; i < data.size(); i++) {
            // 设置默认的显示
            isCheck.put(i, flag);
        }
    }
    public ShopCarAdapter(Context context, List<OrderCarBean> data,OnPriceClickListener listener) {
        super(R.layout.shoppingcar_item_child, data);
        this.context = context;
        this.data = data;
        this.onPriceClickListener = listener;
    }
    //购物车ID
    List<String> obj = new ArrayList<>();
    private List<String>  picUrl = new ArrayList<>();
    private List<Double> priceList = new ArrayList<>();
    private List<Integer> goodsNum = new ArrayList<>();
    Double sumPrice = 0.0;
    @Override
    protected void convert(BaseViewHolder helper, OrderCarBean item) {
        String goods_objId = item.getProduct_objectId();
        query(goods_objId);
        obj.add(item.getObjectId());
        picUrl.add(item.getProduct_picUrl());
        priceList.add(ValueOf.toDouble(item.getProduct_Price()));
        goodsNum.add(item.getProduct_amount());
        int position = helper.getAdapterPosition();
        Log.i(TAG, "convert position: "+ position);

        try {
            helper.setText(R.id.item_chlid_num,item.getProduct_amount()+"");
            helper.setText( R.id.tv_child,item.getProduct_name());
            helper.setText(R.id.item_chlid_money,ValueOf.toString(item.getProduct_Price()));
            Glide.with(mContext).load(item.getProduct_picUrl()).into((ImageView) helper.getView(R.id.item_chlid_image));
        }catch (Exception e){
            Log.e("TAG", "convert: " + e.getMessage());
        }
        helper.addOnClickListener(R.id.cb_child);
        helper.addOnClickListener(R.id.item_chlid_close);
        helper.addOnClickListener(R.id.item_chlid_add);
        helper.addOnClickListener(R.id.cb_child);

        Button item_chlid_num = helper.getView(R.id.item_chlid_num).findViewById(R.id.item_chlid_num);
        helper.getView(R.id.item_chlid_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int item_num = ValueOf.toInt(item_chlid_num.getText().toString());
                getPrice(item_num,position);
                onPriceClickListener.onChangeData();
                if (ValueOf.toInt(item_chlid_num.getText().toString()) > 1) {
                    update(obj.get(position),item_num - 1,picUrl.get(position));
                    helper.setText(R.id.item_chlid_num,item_num - 1 +"");
                    goodsNum.set(position,item_num - 1);
                } else {
                    ToastUtils.show("不能再减啦！");
                }
            }
        });
        helper.getView(R.id.item_chlid_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item_num = ValueOf.toInt(item_chlid_num.getText().toString());
                getPrice(item_num,position);
                onPriceClickListener.onChangeData();
                if (ValueOf.toInt(item_chlid_num.getText().toString()) < num.get(position)){
                    update(obj.get(position),item_num + 1,picUrl.get(position));
                    helper.setText(R.id.item_chlid_num,item_num + 1+"");
                    goodsNum.set(position,item_num + 1);
                }else {
                    ToastUtils.show("不能再加啦！");
                }
            }
        });

        helper.setOnCheckedChangeListener(R.id.cb_child, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int item_num = ValueOf.toInt(item_chlid_num.getText().toString());
                isCheck.put(position, isChecked);
                for (int i =0;i<isCheck.size();i++){
                    Log.i(TAG, "onCheckedChanged: " + isCheck.get(i));
                    if (isCheck.get(i)){
                        sumPrice = sumPrice + item_num*priceList.get(position);
                    }
                }
                getPrice(item_num,position);
                onPriceClickListener.onChangeData();
            }
        });
        // 设置状态
        if (isCheck.get(position) == null) {
            isCheck.put(position, false);
        }
        helper.setChecked(R.id.cb_child,isCheck.get(position));

    }
    // 全选button获取状态
    public Map<Integer, Boolean> getMap() {
        // 返回状态
        return isCheck;
    }
    private List<Integer> num = new ArrayList<>();

    private void query(String mObjectId) {
        BmobQuery<productInfoBean> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(mObjectId, new QueryListener<productInfoBean>() {
            @Override
            public void done(productInfoBean productInfo, BmobException e) {
                if (e == null) {
                    num.add(productInfo.getCurrent_cnt());
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private void update(String mObjectId,int num,String url) {
        OrderCarBean orderCar = new OrderCarBean();
        orderCar.setProduct_amount(num);
        orderCar.setProduct_picUrl(url);
        orderCar.update(mObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e(TAG,"upDateFinish");
                } else {
                    Log.e("BMOB", e.toString());
                }
            }
        });
    }
    private Double getPrice(int num,int position){
        Double sum = 0.0;

        for (int i =0;i<isCheck.size();i++){
            if (isCheck.get(i)){
                Double price = priceList.get(i);
                num = goodsNum.get(i);
                sum = sum + price*num;
            }
        }
        Log.i(TAG, "getPrice: " + sum);
        sumPrice = sum;
        return sum;
    }
    public Double getPriceToActivity(){
        return sumPrice;
    }

}
