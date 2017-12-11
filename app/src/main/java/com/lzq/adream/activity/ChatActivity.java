package com.lzq.adream.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.lzq.adream.R;
import com.lzq.adream.base.BaseActivity;
import com.lzq.adream.fragment.ChatFragment;

/**
 * Created by ${廖昭启} on 2017/12/5.
 */

public class ChatActivity extends BaseActivity {

    private ChatFragment mChatFragment;
    private String mUserId;
    public static ChatActivity mInstance;

    @Override
    public void initView() {
        FrameLayout chatFragment = (FrameLayout) findViewById(R.id.fragment_content);


    }

    public static ChatActivity getInstance() {
        if (mInstance == null) {
            synchronized (ChatActivity.class) {
                if (mInstance == null) {
                    mInstance = new ChatActivity();
                }
            }
        }
        return mInstance;
    }

    @Override
    public int getContentViewById() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        mUserId = getIntent().getExtras().getString("userId");
        mChatFragment = new ChatFragment();
        mChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, mChatFragment).commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (mUserId.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInstance = null;
    }
}
