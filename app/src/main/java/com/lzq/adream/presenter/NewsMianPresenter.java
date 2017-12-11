package com.lzq.adream.presenter;

import android.util.Log;

import com.lzq.adream.AppConstant;
import com.lzq.adream.interfaces.NewsMainContract;
import com.lzq.adream.model.bean.NewsChannelTable;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ${廖昭启} on 2017/11/6.
 */

public class NewsMianPresenter extends NewsMainContract.Presenter {
    @Override
    public void lodeMineChannelsRequest() {
        mModel.lodeMineNewsChannels().subscribe(new Observer<List<NewsChannelTable>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<NewsChannelTable> value) {
                mView.returnMineNewsChannels(value);

            }

            @Override
            public void onError(Throwable e) {
                Log.e("错误信息",e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mRxManage.on(AppConstant.NEWS_CHANNEL_CHANGED, new Consumer<List<NewsChannelTable>>() {
            @Override
            public void accept(List<NewsChannelTable> newsChannelTables) throws Exception {
                if (null != newsChannelTables) {
                    mView.returnMineNewsChannels(newsChannelTables);
                }
            }
        });
    }

}
