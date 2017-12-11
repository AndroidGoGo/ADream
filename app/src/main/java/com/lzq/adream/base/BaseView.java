package com.lzq.adream.base;

public interface BaseView {

    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);
}
