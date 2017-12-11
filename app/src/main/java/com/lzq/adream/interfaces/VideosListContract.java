package com.lzq.adream.interfaces;

import com.lzq.adream.base.BaseModel;
import com.lzq.adream.base.BasePresenter;
import com.lzq.adream.base.BaseView;
import com.lzq.adream.model.bean.VideoData;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ${廖昭启} on 2017/11/10.
 */

public class VideosListContract  {
    public interface  Model  extends BaseModel{
        Observable<List<VideoData>> getVideosListData(String type, int startPage);
    }
    public interface  View  extends BaseView{
        void returnVideosListData(List<VideoData> videoDatas);
    }
   public abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取视频请求
        public abstract void getVideosListDataRequest(String type,int startPage);
    }
}
