package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.productInfoBean;

import java.util.List;

public class GoodsSearchResultAdapter extends BaseQuickAdapter<productInfoBean, BaseViewHolder> {
    private Context mContext;
    public static final int SEALER = 1;// 卖家
    public static final int BUYER = 0;// 加载更多
    private int type = 0;
    public GoodsSearchResultAdapter(Context mContext, @Nullable List<productInfoBean> goodsData, int type) {
        super(R.layout.search_item, goodsData);
        this.mContext = mContext;
        this.type = type;
    }
    public GoodsSearchResultAdapter(Context mContext, @Nullable List<productInfoBean> goodsData) {
        super(R.layout.search_item, goodsData);
        this.mContext = mContext;
    }
    @Override
    protected void convert(BaseViewHolder holder, productInfoBean item) {
        try{
            if (type == 1){
                holder.setVisible(R.id.count,true);
                holder.setText(R.id.count,"库存：" + item.getCurrent_cnt());
            }
            holder.setText(R.id.good_name_tv,item.getProduct_name());
            holder.setText(R.id.goods_price,item.getPrice().toString());
            Glide.with(mContext).load(item.getPicOne().getUrl()).into((ImageView) holder.getView(R.id.goods_img));
        }catch (Exception e){
            Log.e(TAG, "convert: " + e.getMessage());
        }
    }
}
