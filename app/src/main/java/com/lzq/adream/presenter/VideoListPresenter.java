package com.lzq.adream.presenter;

import com.lzq.adream.interfaces.VideosListContract;
import com.lzq.adream.model.bean.VideoData;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${廖昭启} on 2017/11/10.
 */

public class VideoListPresenter extends VideosListContract.Presenter {
    @Override
    public void getVideosListDataRequest(String type, int startPage) {
        mModel.getVideosListData(type, startPage).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }).subscribe(new Observer<List<VideoData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<VideoData> value) {
                mView.returnVideosListData(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
