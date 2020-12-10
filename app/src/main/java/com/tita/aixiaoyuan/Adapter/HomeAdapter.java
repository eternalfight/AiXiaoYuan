package com.tita.aixiaoyuan.Adapter;

import android.view.View;

import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.viewHolder.NetViewHolder;
import com.tita.aixiaoyuan.app.viewHolder.NewTypeViewHolder;
import com.tita.aixiaoyuan.model.BannerData;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

/**
 * <pre>
 *   Created by zhpan on 2020/4/6.
 *   Description:
 * </pre>
 */
public class HomeAdapter extends BaseBannerAdapter<BannerData, BaseViewHolder<BannerData>> {

    @Override
    protected void onBind(BaseViewHolder<BannerData> holder, BannerData data, int position, int pageSize) {
        holder.bindData(data, position, pageSize);
    }

    @Override
    public BaseViewHolder<BannerData> createViewHolder(View itemView, int viewType) {
        if (viewType == BannerData.TYPE_NEW) {
            return new NewTypeViewHolder(itemView);
        }
        return new NetViewHolder(itemView);
    }

    @Override
    public int getViewType(int position) {
        return mList.get(position).getType();
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == BannerData.TYPE_NEW) {
            return R.layout.item_new_type;
        }
        return R.layout.item_net;
    }
}

