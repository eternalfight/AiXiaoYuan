package com.tita.aixiaoyuan.Adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.tita.aixiaoyuan.app.fragment.SearchNestedScrollingBaseFragment;

import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class SearchNestedScrollingFragmentAdapter extends BaseFragmentAdapter<SearchNestedScrollingBaseFragment>{


    public SearchNestedScrollingFragmentAdapter(FragmentManager fm, List<SearchNestedScrollingBaseFragment> fragments) {
        super(fm,fragments);
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
                return "微博";
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(object.getClass().getName().equals(SearchNestedScrollingBaseFragment.class.getName())){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
