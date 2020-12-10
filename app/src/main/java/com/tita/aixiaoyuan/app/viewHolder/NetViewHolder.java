package com.tita.aixiaoyuan.app.viewHolder;

import android.view.View;

import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.example.zhpan.circleviewpager.view.CornerImageView;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.BannerData;
import com.zhpan.bannerview.BaseViewHolder;
import com.zhpan.bannerview.utils.BannerUtils;

public class NetViewHolder extends BaseViewHolder<BannerData> {

    public NetViewHolder(@NonNull View itemView) {
        super(itemView);
        CornerImageView imageView = findView(R.id.banner_image);
        imageView.setRoundCorner(BannerUtils.dp2px(0));
    }

    @Override
    public void bindData(BannerData data, int position, int pageSize) {
        CornerImageView imageView = findView(R.id.banner_image);
        Glide.with(imageView).load(data.getImagePath()).placeholder(R.drawable.placeholder).into(imageView);
        BannerUtils.log("NetViewHolder", "position:" + position);
    }
}