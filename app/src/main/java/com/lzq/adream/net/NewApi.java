package com.lzq.adream.net;


import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.model.bean.CommonResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lzq on 2017/9/22.
 */

public interface NewApi {
    String GETWEATHERDATAS = "getWeatherDatas";//与方法名保持一致,此处通过反射调用
    @POST(UrlBase.GET_WEATHERDATAS_URL)
    @FormUrlEncoded
    Observable<CommonResponse<String>> getWeatherDatas(@FieldMap Map<String, String> map);



    String LIMITBUY = "limitBuy";//与方法名保持一致,此处通过反射调用
    @POST(UrlBase.GET_LIMITBY)
    @FormUrlEncoded
    Observable<BaseResponse<String>> limitBuy(@FieldMap Map<String, String> map);

    String TOP250 = "top250";//与方法名保持一致,此处通过反射调用
    @POST(UrlBase.GET_MOVIE250)
    @FormUrlEncoded
    Observable<BaseResponse<String>> top250(@FieldMap Map<String, String> map);

    //FrmIntelliPhysicalReportInterface.componentInterface


    String COMPONENTINTERFACE = "componentInterface";//与方法名保持一致,此处通过反射调用
    @POST(UrlBase.GET_COMPONENTINTERFACE)
    @FormUrlEncoded
    Observable<BaseResponse<String>> componentInterface(@FieldMap Map<String, String> map);

    String MOBILE = "index";//与方法名保持一致,此处通过反射调用
    @POST(UrlBase.GET_MOBILE)
    @FormUrlEncoded
    Observable<BaseResponse<String>> index(@FieldMap Map<String, String> map);





}
