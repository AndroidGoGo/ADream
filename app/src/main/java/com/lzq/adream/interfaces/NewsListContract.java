package com.lzq.adream.interfaces;

import com.lzq.adream.base.BaseModel;
import com.lzq.adream.base.BasePresenter;
import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public interface NewsListContract {

    interface Model extends BaseModel {
        //请求获取新闻
        Observable<BaseResponse<String>> getNewsListData(String type, final String id, int startPage);
    }
    interface View extends BaseView {
        //返回获取的新闻
        void returnNewsListData(BaseResponse<String> newsSummaries);
        //返回顶部
        void scrolltoTop();
    }
    public abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取新闻请求
        public abstract void getNewsListDataRequest(String type, final String id, int startPage);
    }

}
