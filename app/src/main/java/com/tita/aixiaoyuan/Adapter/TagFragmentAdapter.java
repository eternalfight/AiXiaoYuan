package com.tita.aixiaoyuan.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TagFragmentAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    //数据源
    private List<Fragment> fragments = new ArrayList<Fragment>();

    /**
     * @param fm
     * @param fragments:数据源
     */
    public TagFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    /**
     * viewpager有几个界面
     *
     * @return
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * 获取条目:根据Position返回界面对应的fragment
     *
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "购物";
            case 1:
                return "外卖";
            case 2:
                return "跑腿";
            case 3:
                return "闲置";
            default:
                return "购物";
        }
    }
}

