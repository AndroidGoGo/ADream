package com.lzq.adream.interfaces;


import com.lzq.adream.base.BaseModel;
import com.lzq.adream.base.BasePresenter;
import com.lzq.adream.base.BaseView;
import com.lzq.adream.model.bean.NewsChannelTable;

import java.util.List;

import io.reactivex.Observable;


/**
 * 新闻主页控制器
 */
public interface NewsMainContract {

    interface Model extends BaseModel {
        Observable<List<NewsChannelTable>> lodeMineNewsChannels();
    }

    interface View extends BaseView {
        void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        public abstract void lodeMineChannelsRequest();
    }
}
