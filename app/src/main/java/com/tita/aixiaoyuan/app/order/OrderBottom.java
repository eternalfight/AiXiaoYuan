package com.tita.aixiaoyuan.app.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.tita.aixiaoyuan.app.i.OrderContent;

public class OrderBottom implements OrderContent {
    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public View getView(Context context, View convertView, LayoutInflater inflater) {
        return null;
    }
}
