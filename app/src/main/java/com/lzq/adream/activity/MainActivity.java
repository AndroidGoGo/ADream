package com.lzq.adream.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;

import com.easemob.redpacketsdk.constant.RPConstant;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hyphenate.chat.EMClient;
import com.lzq.adream.App;
import com.lzq.adream.AppConstant;
import com.lzq.adream.Constant;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseActivity;
import com.lzq.adream.comm.PermissionsManager;
import com.lzq.adream.comm.PermissionsResultAction;
import com.lzq.adream.fragment.mian.ContactFragment;
import com.lzq.adream.fragment.mian.HomeFragment;
import com.lzq.adream.fragment.mian.InfomationFragment;
import com.lzq.adream.fragment.mian.VideoFragment;
import com.lzq.adream.fragment.mian.ZoneFragment;
import com.lzq.adream.model.bean.TabEntity;
import com.lzq.adream.utils.ToastUitl;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    private String[] mTitles = {"首页","消息", "聊天","视频","空间"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal,R.drawable.ic_infomation_normal,R.drawable.ic_contacter_normal,R.mipmap.ic_video_normal,R.drawable.ic_my_zone_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected,R.drawable.ic_infomation_selected,R.drawable.ic_contacter_selected, R.mipmap.ic_video_selected,R.drawable.ic_my_zone_selected};
    private CommonTabLayout mCommonTabLayout;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private HomeFragment  mHomeFragment;
    private ContactFragment  mContactFragment;
    private VideoFragment  mVideoFragment;
    private ZoneFragment  mZoneFragment;
    private InfomationFragment  mInfomationFragment;
    private BroadcastReceiver broadcastReceiver;
    private static MainActivity  mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance   = this;
        requestPermissions();
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
        hideBottomUIMenu();

        initTab();
        initFragment(savedInstanceState);
        registerBroadcastReceiver();


    }
    private LocalBroadcastManager broadcastManager;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        intentFilter.addAction(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    /*if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }*/
                }
                //red packet code : 处理红包回执透传消息
                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
                   /* if (conversationListFragment != null){
                        conversationListFragment.refresh();
                    }*/
                }
                //end of red packet code
            }
        };
    }

    @Override
    public void initView() {
        mCommonTabLayout = (CommonTabLayout) findViewById(R.id.tab_layout);
        mCommonTabLayout.measure(0,0);
    }

    @Override
    public int getContentViewById() {
        return R.layout.activity_main;
    }

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mCommonTabLayout.setTabData(mTabEntities);
        //点击监听
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    private void SwitchTo(int position) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(mInfomationFragment);
                transaction.hide(mContactFragment);
                transaction.hide(mVideoFragment);
                transaction.hide(mZoneFragment);
                transaction.show(mHomeFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 1:

                transaction.hide(mHomeFragment);
                transaction.hide(mVideoFragment);
                transaction.hide(mZoneFragment);
                transaction.hide(mContactFragment);

                transaction.show(mInfomationFragment);
                transaction.commitAllowingStateLoss();
                break;
            //聊天
            case 2:
                transaction.hide(mInfomationFragment);

                transaction.hide(mHomeFragment);
                transaction.hide(mVideoFragment);
                transaction.hide(mZoneFragment);
                transaction.show(mContactFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 3:
                transaction.hide(mInfomationFragment);
                transaction.hide(mContactFragment);
                transaction.hide(mHomeFragment);
                transaction.hide(mZoneFragment);
                transaction.show(mVideoFragment);
                transaction.commitAllowingStateLoss();
                break;
            //我的空间
            case 4:
                transaction.hide(mInfomationFragment);
                transaction.hide(mContactFragment);
                transaction.hide(mVideoFragment);
                transaction.hide(mHomeFragment);
                transaction.show(mZoneFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }


    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            mHomeFragment =(HomeFragment)getSupportFragmentManager().findFragmentByTag("HomeFragment");
            mInfomationFragment=(InfomationFragment)getSupportFragmentManager().findFragmentByTag("InfomationFragment");
           mContactFragment  = (ContactFragment)getSupportFragmentManager().findFragmentByTag("ContactFragment");
            mVideoFragment  = (VideoFragment)getSupportFragmentManager().findFragmentByTag("VideoFragment");
            mZoneFragment  = (ZoneFragment)getSupportFragmentManager().findFragmentByTag("ZoneFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            mHomeFragment = new HomeFragment();
            mInfomationFragment = new InfomationFragment();
            mContactFragment = new ContactFragment();
            mVideoFragment = new VideoFragment();
            mZoneFragment = new ZoneFragment();

            transaction.add(R.id.fl_body, mHomeFragment, "HomeFragment");
            transaction.add(R.id.fl_body, mInfomationFragment, "InfomationFragment");

            transaction.add(R.id.fl_body, mContactFragment, "ContactFragment");
            transaction.add(R.id.fl_body, mVideoFragment, "VideoFragment");
            transaction.add(R.id.fl_body, mZoneFragment, "ZoneFragment");
        }
        transaction.commit();
        SwitchTo(0);
        mCommonTabLayout.setCurrentTab(0);
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
           // v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
          //  decorView.setSystemUiVisibility(uiOptions);
            //| View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - App.exitTime > 2000) {

                ToastUitl.show("再按一次退出程序!",1000);
                App.exitTime =  System.currentTimeMillis();//更新firstTime
                return true;
            } else {
               finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // EMClient.getInstance().logout(true);
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  static MainActivity    getInstance(){
        if (mInstance==null){
            synchronized (mInstance){
                mInstance = new MainActivity();
            }
        }
        return mInstance;
    }
    public  static  void closeAct(){
        mInstance.finish();
    }


}
