package com.lzq.adream.rxmanager;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ${廖昭启} on 2017/11/6.
 */

public class RxManager  {
public RxBus  mRxBus  = RxBus.getInstance();
    //管理rxbus订阅
    private Map<String, Observable<?>> mObservables = new HashMap<>();
    /*管理Observables 和 Subscribers订阅*/
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /**
     * RxBus注入监听
     * @param eventName
     * @param consumer
     */
    public <T>void on(String eventName, Consumer<T> consumer) {
        Observable<T> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        /*订阅管理*/
        mCompositeDisposable.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }


                }));
    }

    /**
     * 单一的Observables 和 Subscribers管理
     * @param disposable
     */
    public void add(Disposable disposable) {
        /*订阅管理*/
        mCompositeDisposable.add( disposable);
    }
    public void registerRx(){

    };

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    public void clear() {
        mCompositeDisposable.clear();// 取消所有订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet()) {
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除rxbus观察
        }
    }

    //发送rxbus
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

}
