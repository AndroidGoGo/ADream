package com.lzq.adream.net;


import com.lzq.adream.model.bean.GirlData;
import com.lzq.adream.model.bean.NewsDetail;
import com.lzq.adream.model.bean.NewsList;
import com.lzq.adream.model.bean.NewsSummary;
import com.lzq.adream.model.bean.VideoData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;


/**
 * des:ApiService
 * Created by xsf
 * on 2016.06.15:47
 */
public interface ApiService {


    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetail>> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId);

    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type, @Path("id") String id,
            @Path("startPage") int startPage);//http://c.m.163.com/nc/article/{type}/{id}/{startPage}-20.html?startPage=0&type=list&T1348649580692


    /* String GETWEATHERDATAS = "getWeatherDatas";//与方法名保持一致,此处通过反射调用
     @POST(UrlBase.GET_WEATHERDATAS_URL)
     @FormUrlEncoded
     Observable<CommonResponse<String>> getWeatherDatas(@FieldMap Map<String, String> map);
 */
    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Header("Cache-Control") String cacheControl,
            @Url String photoPath);
    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    // baseUrl 需要符合标准，为空、""、或不合法将会报错

    @GET("data/福利/{size}/{page}")
    Observable<GirlData> getPhotoList(
            @Header("Cache-Control") String cacheControl,
            @Path("size") int size,
            @Path("page") int page);

 /*   @GET("nc/video/list/{type}/n/{startPage}-10.html")   //获取视频列表
    Observable< List<VideoData>> getVideoList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("startPage") int startPage);
*/

    @GET("nc/video/list/{type}/n/{startPage}-10.html")   //获取视频列表
    Observable<List<VideoData>> getVideoList(
            @Path("type") String type,
            @Path("startPage") int startPage);

    String social = "social";
    @POST("/wxnew?key=5112902d41b4afbe45aad32f4128f7a8&num=30&page=1")
    @FormUrlEncoded
    Observable<NewsList<String>> social(

            @FieldMap Map<String, String> map);//https://api.tianapi.com/wxnew/?key=8d6e3228d25298f13af4fc40ce6c9679&num=10&page=

    String getNewsList = "getNewsList";
    @POST("nc/article/{type}/{id}/{startPage}-20.html")
    @FormUrlEncoded
    Observable<Map<String, List<NewsSummary>>> getNewsList(@FieldMap Map<String, String> map);

    String guonei = "guonei";
    @POST("/guonei?key=5112902d41b4afbe45aad32f4128f7a8&num=20&page=3")
    @FormUrlEncoded
    Observable<NewsList<String>> guonei(
            @FieldMap Map<String, String> map);

    String world = "world";
    @POST("/world?key=5112902d41b4afbe45aad32f4128f7a8&num=20&page=3")
    @FormUrlEncoded
    Observable<NewsList<String>> world(
            @FieldMap Map<String, String> map);

    String military = "military";
    @POST("/nba?key=5112902d41b4afbe45aad32f4128f7a8&num=10&page=3")
    @FormUrlEncoded
    Observable<NewsList<String>> nba(
            @FieldMap Map<String, String> map);


    String huabian = "huabian";
    @POST("/huabian?key=5112902d41b4afbe45aad32f4128f7a8&num=10&page=3")
    @FormUrlEncoded
    Observable<NewsList<String>> huabian(
            @FieldMap Map<String, String> map);


    String tiyu = "tiyu";
    @POST("/tiyu?key=5112902d41b4afbe45aad32f4128f7a8&num=10&page=3")
    @FormUrlEncoded
    Observable<NewsList<String>> tiyu(
            @FieldMap Map<String, String> map);
    //http://v.juhe.cn/toutiao/index?type=top&key=APPKEY  	类型,,top(头条，默认),shehui(社会),guonei(国内),
    // guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)

    @POST("toutiao/index")
    @FormUrlEncoded
    Observable<NewsList<String>> toutiao(
            @FieldMap Map<String, String> map);


}
