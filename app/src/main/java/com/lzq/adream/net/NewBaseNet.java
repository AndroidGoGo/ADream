package com.lzq.adream.net;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.interfaces.OnNetResponseListener;
import com.lzq.adream.rxmanager.SetterExclusionStrategy;
import com.lzq.adream.utils.CommonInterfaceReqUtils;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lzq on 2017/9/22.  此处是中转站
 */

public class NewBaseNet {
    String method;
    Observable<BaseResponse<String>> observable;

    OnNetResponseListener<BaseResponse<String>> onNetResponseListener;
    Map<String, Object> params = new ArrayMap<>();
    private boolean needFailedStatus;

    public NewBaseNet setMethod(String method) {
        this.method = method;
        return this;
    }

    public NewBaseNet needFailedStatus() {
        this.needFailedStatus = true;
        return this;
    }

    public NewBaseNet addParam(String key, Object param) {
        params.put(key, param);
        return this;
    }

    public NewBaseNet build() {
        try {
            ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(new String[]{"createTime", "lastUpdateTime", "birthday"});
            Gson gson = new GsonBuilder().setExclusionStrategies(excludeStrategy)
                    .create();//这个是过滤器   一般用不到

//            Method invokeMethod = ZhenlerAPI.class.getDeclaredMethod(method, new Class[]{RequestBody.class,String.class});
            Method invokeMethod = NewApi.class.getDeclaredMethod(method, new Class[]{Map.class});
//            RequestBody body;
            String request = null;

            if (params.size() > 0) {
                request = gson.toJson(params);
            }
            if (TextUtils.isEmpty(request)) {
                observable = (Observable<BaseResponse<String>>) invokeMethod.invoke(RetrofitService.createAPI(1));
            } else {
                Log.v("request参数", "request---------" + request);
                observable = (Observable<BaseResponse<String>>) invokeMethod.invoke(RetrofitService.createAPI(1)
                        , CommonInterfaceReqUtils.initRequestParameters(request));
//                JSONObject obj=new JSONObject(request);
//                body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),request);
//                observable = (Observable<CommonResponse<String>>) invokeMethod.invoke(RetrofitService.createAPI()
//                        , body, CommonInterfaceReqUtils.initRequestParameters(obj));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }


    public NewBaseNet setOnNetResponseListener(OnNetResponseListener<BaseResponse<String>> onNetResponseListener) {
        this.onNetResponseListener = onNetResponseListener;
        return this;
    }

    public void onNetLoad() {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (onNetResponseListener != null)
                            onNetResponseListener.onStart();
                    }
                })
                .subscribe(new Observer<BaseResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BaseResponse<String> value) {
                        //这是判断是否是业务层面的服务器请求异常，此处是根据返回的值是否为空来判断的，根据自己和服务器的约定自行判断业务异常
                        Log.e("BaseModelImp服务器返回---", value.toString());

                        if (needFailedStatus) {
                            if (onNetResponseListener != null)
                                onNetResponseListener.onSuccess(value.getSubjects());
                        } else {
                            if (value.isReturnStatus == true) {
                                if (onNetResponseListener != null)
                                    onNetResponseListener.onSuccess(value.getSubjects());
                            } else {
                                if (onNetResponseListener != null)
                                    onNetResponseListener.onFailure(new Exception("错误码:" + ""));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        //此处属于服务器请求异常！
                        if (onNetResponseListener != null) {
                            onNetResponseListener.onFailure(e);
                            onNetResponseListener.onFinish();
                        }

                    }

                    @Override
                    public void onComplete() {
                        //仅成功后会调用,失败则不会调用
                        if (onNetResponseListener != null)
                            onNetResponseListener.onFinish();
                    }
                });
    }

}
