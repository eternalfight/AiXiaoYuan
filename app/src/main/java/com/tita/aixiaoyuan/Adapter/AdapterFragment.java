package com.tita.aixiaoyuan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.R;

import java.util.List;


public class AdapterFragment extends RecyclerView.Adapter<AdapterFragment.TextHolder> {

    private Context context;
    private List<String> strs;
    private LayoutInflater inflater;

    public AdapterFragment(Context context, List<String> strs) {
        this.context = context;
        this.strs = strs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycleview_good_item, parent, false);
        TextHolder holder = new TextHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(TextHolder holder, int position) {
        String str = strs.get(position);
        holder.textView.setText(str);
    }

    @Override
    public int getItemCount() {
        return strs.size();
    }

    public class TextHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TextHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_item);
        }

    }

}
