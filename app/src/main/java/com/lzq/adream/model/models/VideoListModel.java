package com.lzq.adream.model.models;

import com.lzq.adream.interfaces.VideosListContract;
import com.lzq.adream.model.bean.VideoData;
import com.lzq.adream.net.HostType;
import com.lzq.adream.net.NormalService;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ${廖昭启} on 2017/11/10.
 */

public class VideoListModel implements VideosListContract.Model {
    @Override
    public Observable<List<VideoData>> getVideosListData(String type, int startPage) {
        return NormalService.createAPI(HostType.NETEASE_NEWS_VIDEO).getVideoList(type, startPage);
    }

}
