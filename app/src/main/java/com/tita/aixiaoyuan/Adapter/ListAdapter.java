package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class ListAdapter extends RecyclerView.Adapter{

    private List<String> listData;
    private Context context;
    private LayoutInflater layoutInflater;
    private ItemClickListener itemClickListener;
    public ListAdapter(Context context){
        this.context = context;
        listData = new ArrayList<>();
        for(int i=0;i<50;i++){
            listData.add("Item-"+i);
        }
        layoutInflater = LayoutInflater.from(context);
    }

    public ListAdapter(Context context, List<String> listData){
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(layoutInflater.inflate(R.layout.recycleview_good_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            String name = listData.get(position);
            holder.itemView.setTag(R.id.item_position,position);
            ((ItemViewHolder) holder).tvItemName.setText(name+"");
            /*((ItemViewHolder) holder).tvItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"test item", Toast.LENGTH_SHORT).show();
                }
            });*/
            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"test item", Toast.LENGTH_SHORT).show();
                    if(itemClickListener != null)
                        itemClickListener.onItemClicked(v,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_item)
        TextView tvItemName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemClickListener {
        void onItemClicked(View view,int position);
    }

    //设置点击回调接口
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
