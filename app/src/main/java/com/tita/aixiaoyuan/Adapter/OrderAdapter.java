package com.tita.aixiaoyuan.Adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.MyOrderBean;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<MyOrderBean, BaseViewHolder> {
    List<MyOrderBean> data = new ArrayList<>();
    private String orderState;
    private int i=0;
    public OrderAdapter(@Nullable List<MyOrderBean> data) {
        super(R.layout.order_list_item, data);
        this.data = data;
    }


    @Override
    protected void convert(BaseViewHolder helper, MyOrderBean item) {
        helper.setText(R.id.order_id, "订单号：" + item.getOrderId());
        helper.setText(R.id.tv_good_name, "" + item.getProduct_name().get(i));
        helper.setText(R.id.tv_good_one_price, "" + item.getProduct_price().get(i) + "元");
        helper.setText(R.id.tv_good_number, "" + item.getProduct_cnt().get(i));
        //Glide.with(mContext).load()
        switch (item.getOrder_status()) {
            case 0:
                orderState = "待付款";
                break;
            case 1:
                orderState = "待发货";
                break;
            case 2:
                orderState = "待收货";
                break;
            case 3:
                orderState = "待评价";
                break;
        }
        helper.setText(R.id.order_state, "" + orderState);

    }

}


