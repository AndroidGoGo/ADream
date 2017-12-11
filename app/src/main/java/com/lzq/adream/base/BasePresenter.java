package com.lzq.adream.base;

import android.content.Context;

import com.lzq.adream.rxmanager.RxManager;

/**
 * Created by ${廖昭启} on 2017/11/6.
 */

public class BasePresenter< V,M> {

    public Context mContext;
    public M mModel;//数据模型
    public V mView;//视图
    public RxManager mRxManage = new RxManager();

    public void setVM( V v,M m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart() {
    }

    public void onDestroy() {
        mRxManage.clear();
    }
}
