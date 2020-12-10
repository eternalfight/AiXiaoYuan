package com.tita.aixiaoyuan.app.viewHolder;

import android.view.View;

import androidx.annotation.NonNull;

import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.BannerData;
import com.zhpan.bannerview.BaseViewHolder;

/**
 * <pre>
 *   Created by zhangpan on 2020/4/9.
 *   Description:
 * </pre>
 */
public class NewTypeViewHolder extends BaseViewHolder<BannerData> {

    public NewTypeViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bindData(BannerData data, int position, int pageSize) {
        setImageResource(R.id.image_view, data.getDrawable());
    }
}
