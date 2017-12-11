package com.lzq.adream.activity;

import android.content.Context;
import android.content.Intent;

import com.lzq.adream.AppConstant;
import com.lzq.adream.base.BaseActivity;
import com.lzq.adream.model.bean.NewsPhotoDetail;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public class NewsPhotoDetailActivity extends BaseActivity {
    @Override
    public void initView() {

    }

    @Override
    public int getContentViewById() {
        return 0;
    }

    /**
     * 入口
     *
     * @param context
     * @param mNewsPhotoDetail
     */
    public static void startAction(Context context, NewsPhotoDetail mNewsPhotoDetail) {
        Intent intent = new Intent(context, NewsPhotoDetailActivity.class);
        intent.putExtra(AppConstant.PHOTO_DETAIL, mNewsPhotoDetail);
        context.startActivity(intent);
    }

}
