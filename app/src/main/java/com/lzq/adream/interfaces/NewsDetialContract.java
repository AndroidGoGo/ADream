package com.lzq.adream.interfaces;

import com.lzq.adream.base.BaseModel;
import com.lzq.adream.base.BasePresenter;
import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by ${廖昭启} on 2017/11/9.
 */

public class NewsDetialContract  {
    interface  Model extends BaseModel{
        Observable<BaseResponse<String>> getNewsDetial(String url);
    }
    interface  View extends BaseView{
        void returnNewsDetialDatas(BaseResponse<String>  datas);
    }
    public abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取新闻请求
        public abstract void getNewsDetialRequest(String url);
    }
}
