package com.lzq.adream.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.lzq.adream.App;
import com.lzq.adream.R;
import com.lzq.adream.SPContans;
import com.lzq.adream.base.BaseActivity;
import com.lzq.adream.model.LoginUser;
import com.lzq.adream.utils.SharedPreferencesUtils;
import com.lzq.adream.utils.ToastUitl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    private static final int LOGIN = 1000;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.bt_go)
    Button mBtGo;
    @BindView(R.id.cv)
    CardView mCv;
    /*  @BindView(R.id.fab)
      FloatingActionButton mFab;*/
    @BindView(R.id.btn_qqLogin)
    Button mBtnQqLogin;
    @BindView(R.id.weixinLogin)
    Button mWeixinLogin;
    private String mUserName;
    private LoginActivity mInstance;
    private boolean deBug = false;
    private FloatingActionButton mFab;
    private boolean isAutoLogin;
    private CheckBox mCheckBox;
    private String mPassWord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstance = this;
        initListener();
    }

    public LoginActivity getInstance() {
        if (mInstance == null) {
            synchronized (LoginActivity.class) {
                if (mInstance == null)
                    mInstance = new LoginActivity();
            }
        } else {
            return mInstance;
        }
        return mInstance;
    }

    private void initListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ToastUitl.show("点击了悬浮按钮", 2000);

                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, mFab, mFab.getTransitionName());
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }

            }
        });


    }


    private static final String TAG = "MineLoginActivity";

    public void successBugCode(int error_code) {
        Toast.makeText(this, "错误码:" + error_code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mCheckBox = (CheckBox) findViewById(R.id.cb_savePass);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAutoLogin = isChecked;
            }
        });
        initAutoLogin();
    }

    private void initAutoLogin() {
        boolean isAotuLogin = SharedPreferencesUtils.getBoolean(mContext, SPContans.IS_SAVE_PASS_KEY);
        if (isAotuLogin) {
            mCheckBox.setChecked(isAotuLogin);
            LoginUser loginUser = App.sApp.getLoginUser();
            mEtUsername.setText(loginUser.getName());
            mEtPassword.setText(loginUser.getPass());
            Log.e("自动登录信息===",loginUser.toString());
            onViewClicked(mBtGo);
        }
    }

    @Override
    public int getContentViewById() {
        return R.layout.activity_mine_login;
    }


    private boolean isDownloadedAppMaket() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        List<String> pkgName = new ArrayList<String>();
        if (installedPackages != null) {
            for (int i = 0; i < installedPackages.size(); i++) {
                pkgName.add(installedPackages.get(i).packageName);
            }
        }
        return pkgName.contains("com.hg.skinanalyze");
    }

    @OnClick({R.id.btn_qqLogin, R.id.weixinLogin, R.id.bt_go, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_go:

                mUserName = mEtUsername.getText().toString().trim();
                mPassWord = mEtPassword.getText().toString().trim();
              /*  Intent login = new Intent(this, MainActivity.class);
                startActivity(login);*/
                if (TextUtils.isEmpty(mUserName)) {
                    ToastUitl.show("用户名不能为空", 2000);
                    return;
                }
                if (TextUtils.isEmpty(mPassWord)) {
                    ToastUitl.show("密码不能为空", 2000);
                    return;
                }
                EMClient.getInstance().login(mUserName, mPassWord, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        Message messge = new Message();
                        messge.what = LOGIN;
                        mHandler.sendMessage(messge);
                        EMClient.getInstance().chatManager().loadAllConversations();
                        EMClient.getInstance().groupManager().loadAllGroups();

                        App.sUserName = mUserName;
                        Log.e("login", "登录聊天服务器成功！");
                        LoginUser loginUser = new LoginUser();
                        loginUser.setName(mUserName);
                        loginUser.setPass(mPassWord);
                        if (isAutoLogin) {
                            SharedPreferencesUtils.save(mContext, SPContans.LAST_LOGIN_USER_KEY, JSON.toJSON(loginUser).toString());
                            SharedPreferencesUtils.save(mContext,SPContans.IS_SAVE_PASS_KEY,isAutoLogin);
                        }
                        finish();


                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.e("login", "登录聊天服务器失败！+" + message);
                    }
                });

                break;
            case R.id.fab:

                break;

            case R.id.btn_qqLogin:
                ToastUitl.show("暂未开放", 2000);
                break;
            case R.id.weixinLogin:
                ToastUitl.show("暂未开放", 2000);
                break;
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case LOGIN:
                    ToastUitl.show("登录成功！", 1000);
                    startNewActivity(MainActivity.class);
                    break;
            }
        }
    };
}
