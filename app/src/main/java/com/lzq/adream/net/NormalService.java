package com.lzq.adream.net;

import android.util.Log;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lzq.adream.App;
import com.lzq.adream.utils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${廖昭启} on 2017/11/15.
 */




/**
 * <pre>
 *
 *     网络请求引引擎类，包含网络拦截器，包含网络缓存策略
 *     <pre/>
 *
 *
 * 文件名： MvpRxRetrofit
 * Created by lzq on 2017/8/31.
 */

public class NormalService {

    //设置 数据的缓存时间，有效期为两天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control 配置，为 only-if-cache 时，只查询缓存而不会请求服务器， max-stale可以配合设置缓存失效时间
    protected static final String CACHE_CONTROL_CACHE = "only-if-cache, max-stale=" + CACHE_STALE_SEC;
    //查询缓存的Cache-Control配置，为 Cache-Control设置为max-age=0时则不会使用缓存，而是请求服务器
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    /**
     * 单例模式的 OKhttpClient,以及Retrifit引擎类
     */
    private static OkHttpClient mOkHttpClient;
    private static NormalService instance = null;
    private  static SparseArray<NormalService> mNetManagers = new SparseArray();
    public Retrofit retrofit;

    //私有构造函数，使用newInstance方式访问对象(单利RetrofitService)
    private NormalService() {
    }

    //单利 引擎
    public static NormalService getInstance() {
        if (instance == null) {
            synchronized (RetrofitService.class) {
                if (instance == null) {
                    instance = new NormalService();
                }
            }
        }
        return instance;
    }

    private NormalService(int type){
        if (retrofit == null) {
            synchronized (RetrofitService.class) {
                if (retrofit == null) {
                    initOkHttpClient();
                    retrofit = new Retrofit.Builder()
                            .client(mOkHttpClient)
                            .baseUrl(ApiConstants.getHost(type))
                           // .addConverterFactory(SetterExclusionStrategys.create())//增加返回值为BaseResponse<String>的支持
                            //  .addConverterFactory(ScalarsConverterFactory.create())////增加返回值为String的支持
                            .addConverterFactory(GsonConverterFactory.create(new Gson()))//增加返回值为Gson实体类的支持
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//增加对RxJava操作的支持
                            .build();

                    sApi = retrofit.create(ApiService.class);
                }
            }
        }
    }

    /**
     * API(联网模式),也是单列模式
     */
    private   ApiService sApi = null;

    public static ApiService createAPI(int hostType) {
        NormalService  retrofitService   =  mNetManagers.get(hostType);
        if (retrofitService==null){
            retrofitService = new NormalService(hostType);
            mNetManagers.put(hostType,retrofitService);
        }

        return retrofitService.sApi;
    }


    private static void initOkHttpClient() {
        if (mOkHttpClient == null) {
            //因为涉及,但是okHttpClient的配置是一样的，所以可以用静态创建一次

            File cacheFile = new File(App.sApp.getCacheDir(), "HttpCache");//指定缓存路径
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);//指定缓存最大大小为100Mb
            //云端相应头拦截器，用来动态配置缓存策略
            Interceptor reWriteCacheControlInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    //如果请求的时候没网
                    if (!NetUtil.isConnected(App.sApp)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();//强制读取缓存
                    }


                    /**
                     * 解决okhttp 报java.lang.IllegalStateException: closed,java.lang.IllegalStateException:
                     * closed，原因为OkHttp请求回调中response.body().string()只能有效调用一次
                     */
                    //响应的时候
                    Response originalResponse = chain.proceed(request);
                    //这里获取请求返回的cookie
                    MediaType mediaType = originalResponse.body().contentType();
                    String responseBodyStr = originalResponse.body().string();
                    Log.v("请求结果", "requestUrl：" + request.url() + "-------->responseBody " + responseBodyStr);
                    if (NetUtil.isConnected(App.sApp)) {
                        //有网的时候读取接口上的@Headers里的配置，你可以在这里统一配置
                        String cacheControl = request.cacheControl().toString();
                        return originalResponse.newBuilder()
                                .header("Cache-Control", cacheControl)
                                .body(ResponseBody.create(mediaType, responseBodyStr))
                                .removeHeader("Pragma").build();
                    } else {
                        return originalResponse.newBuilder().header("Cache-Control",
                                "public, only-if-cached," + CACHE_STALE_SEC)
                                .body(ResponseBody.create(mediaType, responseBodyStr))
                                .removeHeader("Pragma")
                                .build();
                    }
                }
            };

            /**
             * okhttp 2.x
             */
//            mOkHttpClient = new OkHttpClient();
//            mOkHttpClient.setCache(cache);
//            hjmOkHttpClient.networkInterceptors().add(rewriteCacheControlInterceptor);
//            mOkHttpClient.interceptors().add(rewriteCacheControlInterceptor);
//            mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
            /**
             * okhttp 3.x
             */
            mOkHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
//                    .addNetworkInterceptor(reWriteCacheControlInterceptor)
                    .addInterceptor(reWriteCacheControlInterceptor)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .build();

        }
    }

}
