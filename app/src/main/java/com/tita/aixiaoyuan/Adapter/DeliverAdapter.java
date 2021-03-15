package com.tita.aixiaoyuan.Adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.DeliverBean;

import java.util.List;

public class DeliverAdapter extends BaseQuickAdapter<DeliverBean, BaseViewHolder> {

    public DeliverAdapter(List<DeliverBean> data) {
        super(R.layout.deliver_item, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, DeliverBean item) {
        try {
            helper.setText(R.id.order_id_tv,item.getOrderID());
            helper.setText(R.id.username,item.getUsername());
            helper.setText(R.id.phone,item.getPhone());
            helper.setText(R.id.addr_tv,"地址："+item.getAddr());
            helper.setText(R.id.goods_tv,"购买商品数量："+item.getNum());
            helper.addOnClickListener(R.id.btn_confirm);
        }catch (Exception e){
            Log.e(TAG, "convert: " + e.getMessage());
        }

    }
}
