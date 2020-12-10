package com.tita.aixiaoyuan.Adapter;

import android.view.View;

import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.viewHolder.NetViewHolder;
import com.tita.aixiaoyuan.model.BannerData;
import com.zhpan.bannerview.BaseBannerAdapter;

public class BannerAdapter extends BaseBannerAdapter<BannerData, NetViewHolder> {
    @Override
    protected void onBind(NetViewHolder holder, BannerData data, int position, int pageSize) {
        holder.bindData(data, position, pageSize);
    }

    @Override
    public NetViewHolder createViewHolder(View itemView, int viewType) {
        return new NetViewHolder(itemView);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_net;
    }
}
