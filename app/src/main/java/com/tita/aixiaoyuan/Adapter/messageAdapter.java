package com.tita.aixiaoyuan.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.MyViewHolder> {

    private List<String> Username = new ArrayList<>();
    private List<Integer> Icns = new ArrayList<>();
    private List<String> Times = new ArrayList<>();
    private List<String> LastMessage = new ArrayList<>();

    public messageAdapter() {
        //addData();//初始化数据
    }

    float mRawX,mRawY;
    private String[] usernames = {"ssss","aaaa","bbbb","ssss","aaaa","bbbb","ssss","aaaa","bbbb","ssss","aaaa","bbbb","ssss","aaaa","bbbb","ssss","aaaa","bbbb","ssss","aaaa","bbbb","ssss","aaaa","bbbb"};
    private int[] icons = {R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head,R.drawable.center_default_head};
    private String[] times = {"20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20","20/19/20"};
    private String[] lastMessages = {"最新消息hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息","最新消息"};



    private OnItemClickListener listener;
    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }
    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnTouchListener onTouchListener;
    public interface OnTouchListener{
        void  onTouch(float mRawX, float mRawY);
    }
    public void setOnTouchListener(OnTouchListener onTouchListener){
        this.onTouchListener = onTouchListener;
    }


    private OnItemLongClickListener longClickListener;
    public interface OnItemLongClickListener {
        void onClick(int position, int[] location);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }



    //添加数据
    public void addData() {
        try {
            //  在list中添加数据，并通知条目加入一条
            Username.addAll(Arrays.asList(usernames));
            Times.addAll(Arrays.asList(times));
            LastMessage.addAll(Arrays.asList(lastMessages));
            int i;
            int p = 0;
            for(i = 0;i<icons.length;i++){
                Icns.add(p,icons[i]);
                p++;
            }
            Log.i("Tag", "addData: "+Username.size()+","+Icns.size()+","+Times.size()+","+LastMessage.size());
            Log.i("Tag", "usernames: "+usernames.length+","+icons.length+","+times.length+","+lastMessages.length);
        }catch (Exception e){
            Log.i("Tag", "Err addData: " + e.getMessage());
        }

    }


    //  删除数据
    public void removeData(int position) {
        Username.remove(position);
        Icns.remove(position);
        Times.remove(position);
        LastMessage.remove(position);
        Log.i("Tag", "removeData: "+Username.size()+","+Icns.size()+","+Icns.size()+","+Times.size()+","+LastMessage.size()+"    ,RemovePosition:"+position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public messageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item,parent,false));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull messageAdapter.MyViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    int[] location = new int[2];
                    holder.itemView.getLocationOnScreen(location);
                    longClickListener.onClick(position,location);
                }
                return true;
            }
        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                onTouchListener.onTouch(mRawX,mRawY);
                return false;
            }
        });
        try{
            holder.username.setText((String)Username.get(position));
            holder.Touxiang.setImageResource((int)Icns.get(position));
            holder.time.setText((String)Times.get(position));
            holder.last_message.setText((String)LastMessage.get(position));
        }catch (Exception e){
            Log.i("Tag", "Err onBindViewHolder: "+ e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        //Log.i("Tag", "getItemCount: "+usernames.length);
        return Username.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView Touxiang;
        TextView username;
        TextView time;
        TextView last_message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Touxiang = itemView.findViewById(R.id.headImage);
            username = itemView.findViewById(R.id.Meessage_username);
            time = itemView.findViewById(R.id.Message_time);
            last_message = itemView.findViewById(R.id.latest_message);

        }

    }


}

