package com.lzq.adream.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.lzq.adream.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${廖昭启} on 2017/11/6.
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    private List<String> mTitles;
    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> newsChannelsMine, List<String> tabTitleList) {
        super(fm);
        mTitles = tabTitleList;
        setFragments(fm,newsChannelsMine,tabTitleList);

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }




    //刷新fragment
    public void setFragments(FragmentManager fm,List<Fragment> fragments,List<String> mTitles) {
        this.mTitles = mTitles;
        if (this.fragmentList != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragmentList) {
                ft.remove(f);
            }
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragmentList = fragments;
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return !CollectionUtils.isNullOrEmpty(mTitles) ? mTitles.get(position) : "";
    }
}
