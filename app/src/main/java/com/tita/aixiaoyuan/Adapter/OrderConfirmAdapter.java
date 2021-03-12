package com.tita.aixiaoyuan.Adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.EventMsg;
import com.tita.aixiaoyuan.model.OrderCarBean;
import com.tita.aixiaoyuan.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

public class OrderConfirmAdapter extends BaseQuickAdapter<OrderCarBean, BaseViewHolder> {
    private List<Double> allPrice = new ArrayList<>();
    private Double price = 0.00;

    public OrderConfirmAdapter(List<OrderCarBean> data) {
        super(R.layout.pay_activity_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderCarBean item) {
        //allPrice.add(Double.valueOf(item.getProduct_Price()));
        price += Double.parseDouble(item.getProduct_Price())*item.getProduct_amount();
        try {
            helper.setText(R.id.tv_good_name,item.getProduct_name());
            helper.setText(R.id.tv_good_one_price,item.getProduct_Price());
            helper.setText(R.id.tv_good_number,"x"+item.getProduct_amount());
            Glide.with(mContext).load(item.getProduct_picUrl()).into((ImageView) helper.getView(R.id.goods_img));
        }catch (Exception e){
            Log.e(TAG, "convert: " + e.getMessage());
        }
        initPrice();
    }

    private void initPrice(){
       /* for (int i=0;i<allPrice.size();i++){

        }*/
        EventMsg eventMsg = new EventMsg();
        eventMsg.setMsg(""+price);
        RxBus.getInstance().post(eventMsg);
    }



}
