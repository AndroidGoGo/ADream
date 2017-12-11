package com.lzq.adream.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseActivity;
import com.lzq.adream.utils.ToastUitl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GSXX on 2017/6/19 0019.
 */

public class RegisterActivity extends BaseActivity {
    private static final int TO_LOGIN = 1001;
    private static final int REGISTER_FIAL = 1002;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_repeatpassword)
    EditText mEtRepeatpassword;
    @BindView(R.id.bt_go)
    Button mBtGo;
    @BindView(R.id.cv_add)
    CardView mCvAdd;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    private String mUserName;
    private  boolean  isDebug;


    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                mCvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String registerMsg = (String) msg.obj;
            ToastUitl.show(registerMsg,2000);



            switch (msg.what) {
                case TO_LOGIN:
                    ToastUitl.show("注册成功",2000);
                    mFab.performClick();
                    break;
                case REGISTER_FIAL:
                    ToastUitl.show("注册失败",2000);
            }
        }
    };

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mFab.getWidth() / 2, mCvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                mCvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(mCvAdd, mCvAdd.getWidth() / 2, 0, mCvAdd.getHeight(), mFab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                mFab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @OnClick({R.id.bt_go})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_go:
                mUserName = mEtUsername.getText().toString().trim();
                String passWord = mEtPassword.getText().toString().trim();
                final String comfiPassWord = mEtRepeatpassword.getText().toString().trim();
                if (passWord.equals(comfiPassWord)) {
                    if (comfiPassWord.length() < 6) {
                        mEtPassword.setError("密码长度不能小于6位");
                        return;
                    } else {

/*
                        DaoSession daoSession = App.getDaoSession();
                        UserBeanDao userBeanDao = daoSession.getUserBeanDao();
                        UserBean userBean = new UserBean();
                        userBean.setUserName(mUserName);
                        userBean.setPassWord(comfiPassWord);
                        userBeanDao.insert(userBean);*/

                /*Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                finish();*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().createAccount(mUserName, comfiPassWord);

                                } catch (HyphenateException e) {
                                    e.printStackTrace();

                                    Message message = new Message();
                                    message.obj =e.getMessage();
                                    message.what = REGISTER_FIAL;
                                    mHandler.sendMessage(message);
                                    Log.e("注册结果信息",e.getMessage());
                                    return;
                                }

                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                                Message message = new Message();
                                message.what = TO_LOGIN;
                                mHandler.sendMessage(message);

                            }
                        }).start();
                    }
                } else {
                    mEtPassword.setError("两次输入不一致");
                }
        }
    }


    public void successBugCode(int error_code) {
        Toast.makeText(this, "错误码:" + error_code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @Override
    public int getContentViewById() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


}
