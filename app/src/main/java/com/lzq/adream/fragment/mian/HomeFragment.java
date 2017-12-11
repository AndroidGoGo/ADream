package com.lzq.adream.fragment.mian;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lzq.adream.AppConstant;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseFragment;
import com.lzq.adream.base.BaseFragmentAdapter;
import com.lzq.adream.fragment.NewsFragment;
import com.lzq.adream.interfaces.NewsMainContract;
import com.lzq.adream.model.bean.NewsChannelTable;
import com.lzq.adream.model.models.NewsMainModel;
import com.lzq.adream.presenter.NewsMianPresenter;
import com.lzq.adream.utils.TabLayoutUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public class HomeFragment extends BaseFragment<NewsMianPresenter, NewsMainModel> implements NewsMainContract.View {


    private TabLayout mTabTitle;
    private BaseFragmentAdapter  mAdapter;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        mPresenter.lodeMineChannelsRequest();
        mTabTitle = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        initlistener();


    }

    private void initlistener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine) {
        List<String> tabTitleList = new ArrayList<>();
        List<Fragment> mNewsFragmentList = new ArrayList<>();
        if (newsChannelsMine != null) {
            for (int i = 0; i < newsChannelsMine.size(); i++) {
                NewsChannelTable newsChannelTable = newsChannelsMine.get(i);
                String newsChannelName = newsChannelTable.getNewsChannelName();

                tabTitleList.add(newsChannelName);
                mNewsFragmentList.add(createListFragments(newsChannelsMine.get(i)));
              /*  Log.e("异步获取头部标题", newsChannelName);
                Log.e("异步获取头部类型",  newsChannelTable.getNewsChannelType());
                Log.e("异步获取头部id",  newsChannelTable.getNewsChannelId());
                Log.e("分割线",  "----------------------------------------------");*/

              //  mTabTitle.addTab(mTabTitle.newTab().setText(newsChannelName));


            }
            if(mAdapter==null) {
              //  Log.e("添加fragment","true");
                mAdapter = new BaseFragmentAdapter(getChildFragmentManager(), mNewsFragmentList, tabTitleList);
            }else{
                //刷新fragment
              //  Log.e("刷新fragment","true");
                mAdapter.setFragments(getChildFragmentManager(),mNewsFragmentList,tabTitleList);
            }
            mViewPager.setAdapter(mAdapter);
          mTabTitle.setupWithViewPager(mViewPager);
            TabLayoutUtil.dynamicSetTabLayoutMode(mTabTitle);

        }


    }

    private Fragment createListFragments(NewsChannelTable newsChannelTable) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.NEWS_ID, newsChannelTable.getNewsChannelId());
        bundle.putString(AppConstant.NEWS_TYPE, newsChannelTable.getNewsChannelType());
        bundle.putInt(AppConstant.CHANNEL_POSITION, newsChannelTable.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;

    }
}
