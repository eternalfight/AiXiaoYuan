package com.tita.aixiaoyuan.Adapter;

import android.view.View;

import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.viewHolder.ImageResourceViewHolder;
import com.zhpan.bannerview.BaseBannerAdapter;

/**
 * <pre>
 *   Created by zhpan on 2020/4/5.
 *   Description:
 * </pre>
 */
public class ImageResourceAdapter extends BaseBannerAdapter<Integer, ImageResourceViewHolder> {

    private int roundCorner;

    public ImageResourceAdapter(int roundCorner) {
        this.roundCorner = roundCorner;
    }


    @Override
    protected void onBind(ImageResourceViewHolder holder, Integer data, int position, int pageSize) {
        holder.bindData(data, position, pageSize);
    }

    @Override
    public ImageResourceViewHolder createViewHolder(View itemView, int viewType) {
        return new ImageResourceViewHolder(itemView, roundCorner);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_slide_mode;
    }
}
