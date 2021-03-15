package com.tita.aixiaoyuan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tita.aixiaoyuan.Adapter.HomePagerAdapter;
import com.tita.aixiaoyuan.R;
import com.tita.aixiaoyuan.app.fragment.Fragment_all_order;
import com.tita.aixiaoyuan.app.fragment.Fragment_wait_for_deliver;
import com.tita.aixiaoyuan.app.fragment.Fragment_wait_for_evaluate;
import com.tita.aixiaoyuan.app.fragment.Fragment_wait_for_pay;
import com.tita.aixiaoyuan.app.fragment.Fragment_wait_for_recive;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;

public class my_orderActivity extends AppCompatActivity {

    @BindView(R.id.my_order_backbtn)
    TextView rbOrderBack;
    @BindView(R.id.rb_all_order)
    RadioButton rbAllOrder;
    @BindView(R.id.rb_wair_for_pay)
    RadioButton rbWairForPay;
    @BindView(R.id.rb_wair_for_deliver)
    RadioButton rbWairForDeliver;
    @BindView(R.id.rb_wair_for_revice)
    RadioButton rbWairForRevice;
    @BindView(R.id.rb_wair_for_evaluate)
    RadioButton rbWairForEvaluate;
    @BindView(R.id.myorder_view_pager)
    ViewPager myorderViewPager;
    private int fragmentFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initData();
        Intent intent = getIntent();
        fragmentFlag = intent.getIntExtra("fragment_flag", 0);
        SetPage(fragmentFlag);

    }


    private final void initData() {
        // 给ViewPager设置Adapter
        ArrayList<Fragment> fragments = new ArrayList<>();

        // 1.往集合里面添加Fragment
        fragments.add(new Fragment_all_order());
        fragments.add(new Fragment_wait_for_pay());
        fragments.add(new Fragment_wait_for_deliver());
        fragments.add(new Fragment_wait_for_recive());
        fragments.add(new Fragment_wait_for_evaluate());
        // 2.适配器
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), fragments);

        // 3.适配器 交给 ViewPager
        myorderViewPager.setAdapter(homePagerAdapter);
        myorderViewPager.setOffscreenPageLimit(5);

    }

    @OnClick(R.id.my_order_backbtn)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.rb_all_order, R.id.rb_wair_for_pay, R.id.rb_wair_for_deliver, R.id.rb_wair_for_revice, R.id.rb_wair_for_evaluate})
    public void onViewCheckedChanged(View view) {
        switch (view.getId()) {
            case R.id.rb_all_order:
                myorderViewPager.setCurrentItem(0,false);
                break;
            case R.id.rb_wair_for_pay:
                myorderViewPager.setCurrentItem(1,false);
                break;
            case R.id.rb_wair_for_deliver:
                myorderViewPager.setCurrentItem(2,false);
                break;
            case R.id.rb_wair_for_revice:
                myorderViewPager.setCurrentItem(3,false);
                break;
            case R.id.rb_wair_for_evaluate:
                myorderViewPager.setCurrentItem(4,false);
                break;
        }
    }

    @OnPageChange(R.id.myorder_view_pager)
    public void onPageSelected(int position) {
        // 改变四个菜单的效果
        // 切换到相应页面之后调用  postion 当前的位置
        switch (position){
            case 0:
                rbAllOrder.setChecked(true);
                break;
            case 1:
                rbWairForPay.setChecked(true);
                break;
            case 2:
                rbWairForDeliver.setChecked(true);
                break;
            case 3:
                rbWairForRevice.setChecked(true);
                break;
            case 4:
                rbWairForEvaluate.setChecked(true);
                break;
        }
    }
    public void SetPage(int page){
        myorderViewPager.setCurrentItem(page,false);
    }
}
