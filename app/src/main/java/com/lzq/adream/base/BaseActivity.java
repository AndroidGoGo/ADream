package com.lzq.adream.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;

/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected  Context  mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getContentViewById());
        ButterKnife.bind(this);
        initView();


    }
    public abstract void initView();

    public abstract int getContentViewById();

    /**
     * @param clazz
     * @param bundle
     * 不带返回值的页面跳转
     */
    public void startNewActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     * @param clazz

     * 不带返回值的页面跳转
     */
    public void startNewActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);

        startActivity(intent);
    }
    /**
     * @param tClass
     * @param bundle
     * @param requestCode
     * 带返回值的页面跳转
     */
    public void startNewActivityForResult(Class<?> tClass,Bundle bundle,int requestCode){
        Intent  intent = new Intent(this,tClass);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);

    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
