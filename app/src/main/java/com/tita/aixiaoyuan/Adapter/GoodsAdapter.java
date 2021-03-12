package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.productInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GoodsAdapter extends BaseQuickAdapter<productInfoBean, BaseViewHolder> {


    private Context mContext;
    private List<String> PicUrl;

    public GoodsAdapter(Context mContext, List<productInfoBean> goodsData) {
        super(R.layout.recycleview_good_item, goodsData);
        this.mContext = mContext;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, productInfoBean productInfo) {
        try{
            PicUrl = productInfo.getPicUrl();
            holder.setText(R.id.tv_item,productInfo.getProduct_name());
            holder.setText(R.id.item_price,productInfo.getPrice().toString());
            Glide.with(mContext).load(productInfo.getPicUrl().get(0)).into((ImageView) holder.getView(R.id.item_img));
        }catch (Exception e){
            Log.e(TAG, "convert: " + e.getMessage());
        }


    }



}
