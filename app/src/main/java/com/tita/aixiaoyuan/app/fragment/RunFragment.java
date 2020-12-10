package com.tita.aixiaoyuan.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import com.tita.aixiaoyuan.R;

public class RunFragment extends androidx.fragment.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, null);
        //ButterKnife.bind(this, view);
        return view;
    }
}
