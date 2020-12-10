package com.tita.aixiaoyuan.app.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tita.aixiaoyuan.Adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonghongliang on 16/5/8.
 * To fix a issue which action bar does not collapse in layout the CoordinatorLayout -> AppBarLayout -> Toobar -> ViewPager -> RecyclerView -> Cardview -> RecyclerView
 */
public class FixedDataScrollDisabledRecyclerView extends RecyclerView {
    public FixedDataScrollDisabledRecyclerView(Context context) {
        super(context);
        init();
    }

    public FixedDataScrollDisabledRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FixedDataScrollDisabledRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint("WrongConstant")
    private void init(){
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecyclerAdapter bannerAdapter = new RecyclerAdapter(getBanner());
        setAdapter(bannerAdapter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    private List<String> getBanner() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add("item  " + i);
        }
        return data;
    }
}

