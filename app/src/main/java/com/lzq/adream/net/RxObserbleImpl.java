package com.lzq.adream.net;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzq.adream.rxmanager.SetterExclusionStrategy;
import com.lzq.adream.utils.CommonInterfaceReqUtils;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public class RxObserbleImpl<T> {
    String  mCache;
    String method;
    Observable<T> mObservable;
    Map<String, Object> params = new ArrayMap<>();
    int mBaseUrl;

    public  RxObserbleImpl  setCache(String  cache){
        mCache  =cache;
        return this;
    }

    public RxObserbleImpl addParam(String key, Object param) {
        params.put(key, param);
        return this;
    }

    public RxObserbleImpl setMethod(String method) {
        this.method = method;
        return this;
    }

    public RxObserbleImpl setBaseUrl(int urlType) {
        mBaseUrl = urlType;
        return this;
    }


    public  Observable<T> build() {
        Method invokeMethod = null;
        try {
            if (null != method) {
                Log.e("反射方法", method);
            }
            invokeMethod = ApiService.class.getDeclaredMethod(method, new Class[]{Map.class});
            ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(new String[]{"createTime", "lastUpdateTime", "birthday"});
            Gson gson = new GsonBuilder().setExclusionStrategies(excludeStrategy).create();
            String request = null;

            if (params.size() > 0) {
                request = gson.toJson(params);
            }
            Log.e("....", "................");
            mObservable = (Observable<T>) invokeMethod.invoke(RetrofitService.createAPI(mBaseUrl), CommonInterfaceReqUtils.initRequestParameters(request));
            Log.e("....", "................");//RetrofitService.createAPI(HostType.NEWS_DETAIL_HTML_PHOTO)
        } catch (IllegalAccessException e) {
            e.printStackTrace();


        } catch (InvocationTargetException e) {
            e.printStackTrace();


        } catch (NoSuchMethodException e) {
            e.printStackTrace();


        } catch (JSONException e) {

            e.printStackTrace();


        } finally {
            if (mObservable == null) {
                Log.e("观察者", "创建失败");
            } else {
                Log.e("观察者", "创建成功" + mObservable.toString());
            }
        }

        return mObservable;

    }

  public Observable<T>  onNetload() {
        if (mObservable==null){
            Log.e("观察者","空的......");
            return null ;
        }

        mObservable.subscribeOn(Schedulers.io());
        mObservable.observeOn(AndroidSchedulers.mainThread());
        /*mObservable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {

            }
        });
        mObservable.subscribe(new Observer<T>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T value) {

            }



            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });*/
        return mObservable;
    }

    public Observable<T> getObservables() {
        if (mObservable == null) {
            return null;
        }
        return mObservable;
    }
}
