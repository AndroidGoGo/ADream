package com.lzq.adream.fragment.mian;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lzq.adream.AppConstant;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseFragment;
import com.lzq.adream.base.BaseFragmentAdapter;
import com.lzq.adream.fragment.VideosFragment;
import com.lzq.adream.manager.VideosChannelTableManager;
import com.lzq.adream.model.bean.VideoChannelTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public class VideoFragment  extends BaseFragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionButton mFabBtn;
    private BaseFragmentAdapter mBaseAdapter ;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_video;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mFabBtn = (FloatingActionButton) findViewById(R.id.fab);


        initFragment();
        initViewPage();
    }

    private void initViewPage() {
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

    private void initFragment() {
        List<String> channelNames = new ArrayList<>();
        List<VideoChannelTable> videoChannelTableList = VideosChannelTableManager.loadVideosChannelsMine();
        List<Fragment> mNewsFragmentList = new ArrayList<>();
        for (int i = 0; i < videoChannelTableList.size(); i++) {
            mNewsFragmentList.add(creatFragments(videoChannelTableList.get(i)));
            channelNames.add(videoChannelTableList.get(i).getChannelName());
        }

        mBaseAdapter  = new BaseFragmentAdapter(getChildFragmentManager(),mNewsFragmentList,channelNames);
        mViewPager.setAdapter(mBaseAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private VideosFragment creatFragments(VideoChannelTable videoChannelTable) {
        VideosFragment fragment = new VideosFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.VIDEO_TYPE, videoChannelTable.getChannelId());
        fragment.setArguments(bundle);
        return fragment;
    }
   // private  List<String> videoIdList = new ArrayList<>();
    private String  [] videoList;
    public  void  initVideoId(){
     if (   videoList==null){
         videoList = new String[]{"-101","-104","-301",};
     }
    }
}
