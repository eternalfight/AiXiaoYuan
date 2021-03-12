package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tita.aixiaoyuan.Adapter.MySellViewPageAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.fragment.MyPublishFragmemt;
import com.tita.aixiaoyuan.app.fragment.MySellFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class myDealActivity extends AppCompatActivity {
    @BindView(R.id.Mydeal_tablayout)
    TabLayout Mydeal_tablayout;
    @BindView(R.id.Mydeal_viewpage)
    ViewPager Mydeal_viewpage;
    @BindView(R.id.my_deal_backbtn)
    TextView my_deal_backbtn;
    MySellViewPageAdapter myFragmentPagerAdapter;
    private int fragmentFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deal);
        ButterKnife.bind(this);
        initData();
        Intent intent = getIntent();
        fragmentFlag = intent.getIntExtra("fragment_flag", 0);
        SetPage(fragmentFlag);
    }

    private void initData(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyPublishFragmemt());
        fragments.add(new MySellFragment());

        Mydeal_tablayout.setupWithViewPager(Mydeal_viewpage);
        myFragmentPagerAdapter = new MySellViewPageAdapter(getSupportFragmentManager(),fragments);
        Mydeal_viewpage.setAdapter(myFragmentPagerAdapter);
        Mydeal_viewpage.setOffscreenPageLimit(1);
        Mydeal_tablayout.setupWithViewPager(Mydeal_viewpage);
    }

    public void SetPage(int page){
        Mydeal_viewpage.setCurrentItem(page,false);
    }

    @OnClick(R.id.my_deal_backbtn)
    public void onViewClicked() {
        finish();
    }
}