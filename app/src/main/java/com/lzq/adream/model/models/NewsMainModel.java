package com.lzq.adream.model.models;


import android.util.Log;

import com.lzq.adream.App;
import com.lzq.adream.AppConstant;
import com.lzq.adream.interfaces.NewsMainContract;
import com.lzq.adream.manager.NewsChannelTableManager;
import com.lzq.adream.model.bean.NewsChannelTable;
import com.lzq.adream.utils.ACache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${廖昭启} on 2017/11/6.
 */
public class NewsMainModel implements NewsMainContract.Model {
    @Override
    public Observable<List<NewsChannelTable>> lodeMineNewsChannels() {
        return Observable.create(new ObservableOnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewsChannelTable>> observableEmitter) throws Exception {
                ArrayList<NewsChannelTable> newsChannelTableList = (ArrayList<NewsChannelTable>) ACache.get(App.getAppContext()).getAsObject(AppConstant.CHANNEL_MINE);
                if (newsChannelTableList == null) {
                    newsChannelTableList = (ArrayList<NewsChannelTable>) NewsChannelTableManager.loadNewsChannelsStatic();
                    ACache.get(App.getAppContext()).put(AppConstant.CHANNEL_MINE, newsChannelTableList);
                }
                observableEmitter.onNext(newsChannelTableList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e("RxJava", "开始获取固定列表。。。。");
                    }
                });
    }
   /* @Override
    public Observable<List<NewsChannelTable>> lodeMineNewsChannels() {

        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                ArrayList<NewsChannelTable> newsChannelTableList = (ArrayList<NewsChannelTable>) ACache.get(App.getAppContext()).getAsObject(AppConstant.CHANNEL_MINE);
               if(newsChannelTableList==null){
                   newsChannelTableList= (ArrayList<NewsChannelTable>) NewsChannelTableManager.loadNewsChannelsStatic();
                   ACache.get(App.getAppContext()).put(AppConstant.CHANNEL_MINE,newsChannelTableList);
               }
                subscriber.onNext(newsChannelTableList);
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<List<NewsChannelTable>>io_main());
    }*/
}
