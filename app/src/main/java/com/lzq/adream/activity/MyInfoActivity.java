package com.lzq.adream.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.lzq.adream.R;
import com.lzq.adream.SPContans;
import com.lzq.adream.base.BaseActivity;
import com.lzq.adream.utils.SharedPreferencesUtils;
import com.lzq.adream.utils.forview.ImageLoaderUtils;
import com.lzq.adream.widget.view.WaveView;

/**
 * Created by ${廖昭启} on 2017/12/4.
 */

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    private WaveView waveView;
    private ImageView mMeImageView;
    private TextView mTvLoginOut;

    @Override
    public void initView() {

        waveView = (WaveView) findViewById(R.id.wave_view);
        mMeImageView = (ImageView) findViewById(R.id.big_headImage);
        mTvLoginOut = (TextView) findViewById(R.id.tv_loignOut);

        ImageLoaderUtils.displayRound(this, mMeImageView, R.drawable.zly);


        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.CENTER;
        waveView.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {
                lp.setMargins(0, 0, 0, (int) y + 2);
                mMeImageView.setLayoutParams(lp);
            }
        });
        initListener();
    }

    private void initListener() {
        mTvLoginOut.setOnClickListener(this);
    }

    @Override
    public int getContentViewById() {
        return R.layout.fragment_conver;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_loignOut:
                EMClient.getInstance().logout(true);
                SharedPreferencesUtils.save(mContext, SPContans.IS_SAVE_PASS_KEY,false);
                startNewActivity(LoginActivity.class);

                finish();
                MainActivity.closeAct();
                break;
        }
    }
}
