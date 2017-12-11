package com.lzq.adream.presenter;

import android.util.Log;

import com.lzq.adream.AppConstant;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.interfaces.NewsListContract;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public class NewsListPresenter extends NewsListContract.Presenter {
    @Override
    public void getNewsListDataRequest(String type, String id, int startPage) {
        mModel.getNewsListData(type, id, startPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("接收新闻列表Rx请求", "true");
                    }
                })
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                   public void onNext( BaseResponse value) {
                        mView.returnNewsListData(value);
                        Log.e("返回的数据", value.toString());
                    }


                    @Override
                    public void onError(Throwable e) {
                        Log.e("错误信息", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.e("Rxjava", "开始获取新闻列表");
                mView.scrolltoTop();
                mView.showLoading(mContext.getString(R.string.loading));
            }
        });

    }
}
