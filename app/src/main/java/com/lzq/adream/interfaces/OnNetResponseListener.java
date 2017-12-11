package com.lzq.adream.interfaces;

/**
 * <pre>
 *
 *
 *
 *
 *      <pre/>
 *
 * 文件名： MvpRxRetrofit
 * Created by lzq on 2017/3/31.

 */

public interface OnNetResponseListener<T> {
    /**
     * 网络请求开始
     */
    void onStart();

    /**
     * 网络请求结束
     */
    void onFinish();

    /**
     * 网络请求成功
     * @param data 返回的数据实体类信息 泛型定义
     */
    void onSuccess(String data);

    /**
     * 请求失败
     * @param t 异常
     */
    void onFailure(Throwable t);
}
