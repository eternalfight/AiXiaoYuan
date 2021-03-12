package com.tita.aixiaoyuan.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.MessageDataBean;

import java.util.ArrayList;
import java.util.List;

public class MesAdapter extends BaseQuickAdapter<MessageDataBean, BaseViewHolder> {
    List<MessageDataBean> data =new ArrayList<>();

    public MesAdapter(List<MessageDataBean> data) {
        super(R.layout.message_list_item, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageDataBean item) {
        helper.setText(R.id.Meessage_username,item.getUsernames());
        helper.setText(R.id.Message_time,item.getTimes());
        helper.setText(R.id.latest_message,item.getLastMessage());
        Glide.with(mContext).load(item.getIcns()).into((ImageView) helper.getView(R.id.headImage));
    }

}
