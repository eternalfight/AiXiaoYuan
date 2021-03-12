package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.model.OrderBean;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<OrderBean.DataBean> data;
    Context context;

    public OrderAdapter(Context context,List<OrderBean.DataBean> data){
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        OrderBean.DataBean dataBean = data.get(position);
        myViewHolder.goodsName.setText(/*dataBean.getTitle()*/"显卡");
        int status = 1/*dataBean.getStatus()*/;
        myViewHolder.tvBt.setText("查看订单");
        if (status == 0){
            myViewHolder.orderState.setText("待支付");
            myViewHolder.tvBt.setText("取消订单");
        } else if (status == 1) {
            myViewHolder.orderState.setText("已取消");
        } else if (status == 2) {
            myViewHolder.orderState.setText("已支付");
        }
        myViewHolder.goodsPrice.setText(/*dataBean.getPrice()*/"111" + "元");
    }



    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView goodsName;
        TextView goodsSelect;
        TextView goodsNumber;
        TextView goodsPrice;
        TextView orderState;
        TextView tvBt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.goods_img);
            goodsName = itemView.findViewById(R.id.tv_good_name);
            goodsSelect = itemView.findViewById(R.id.tv_good_select);
            goodsNumber = itemView.findViewById(R.id.tv_good_number);
            goodsPrice = itemView.findViewById(R.id.tv_good_one_price);
            orderState = itemView.findViewById(R.id.order_state);
            tvBt = itemView.findViewById(R.id.tvBt);
        }
    }
}
