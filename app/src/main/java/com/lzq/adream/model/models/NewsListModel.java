package com.lzq.adream.model.models;

import com.lzq.adream.AppHelp;
import com.lzq.adream.AppUtils;
import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.interfaces.NewsListContract;
import com.lzq.adream.net.HostType;
import com.lzq.adream.net.RxObserbleImpl;

import io.reactivex.Observable;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public class NewsListModel implements NewsListContract.Model {

    @Override
    public Observable<BaseResponse<String>> getNewsListData(String type, final String id, int startPage) {
        return new RxObserbleImpl<BaseResponse<String>>()
                .setBaseUrl(HostType.NEWS_LIST)
                .setMethod(id)
                .addParam("key", AppHelp.APP_KEY_BY_JUHE_TOUTIAO)
                .addParam("type", "top")
                .addParam("page", 2)
                .setCache(AppUtils.getCacheControl())
                .build();



    }
}
