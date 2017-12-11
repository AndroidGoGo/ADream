package com.lzq.adream.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzq.adream.AppConstant;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseActivity;

import static com.lzq.adream.R.id.toolbar;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public class NewsDetailActivity extends BaseActivity {


    private Toolbar mToobar;
    private WebView mWebView;
    private CollapsingToolbarLayout mToolbarLayout;
    private FloatingActionButton mFab;
    private String mShareLink;

    @Override
    public int getContentViewById() {
        return R.layout.act_news_detail;
    }


    /**
     * 入口
     *
     * @param mContext
     * @param postId
     */
    public static void startAction(Context mContext, View view, String postId, String imgUrl, String title) {
        Intent intent = new Intent(mContext, NewsDetailActivity.class);
        intent.putExtra(AppConstant.NEWS_POST_ID, postId);
        intent.putExtra(AppConstant.NEWS_IMG_RES, imgUrl);
        intent.putExtra("TITLE", title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) mContext, view, AppConstant.TRANSITION_ANIMATION_NEWS_PHOTOS);
            mContext.startActivity(intent, options.toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity((Activity) mContext, intent, options.toBundle());
        }

    }

    @Override
    public void initView() {
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mToobar = (Toolbar) findViewById(toolbar);


        mWebView = (WebView) findViewById(R.id.wb_newsDetial);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        initWebView();
        initHeadImageView();
        initToolbarLayout();
        initListener();
    }

    /**
     * 初始化工具栏
     */
    private void initToolbarLayout() {
        String title = getIntent().getStringExtra("TITLE");
        mToolbarLayout.setTitle(title);
        mToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.white));
        mToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }

    /**
     * 初始化头部图片
     */
    private void initHeadImageView() {

        String imgSrc = getIntent().getStringExtra(AppConstant.NEWS_IMG_RES);
        ImageView ivHead = (ImageView) findViewById(R.id.news_detail_photo_iv);
        if (imgSrc != null) {
            Glide.with(this).load(imgSrc).into(ivHead);
        }
    }

    /**
     * 初始化webView
     */
    private void initWebView() {
        String url = getIntent().getStringExtra(AppConstant.NEWS_POST_ID);
        WebSettings settings = mWebView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setSupportZoom(true);               //是否支持屏幕双击缩放，但是下边的是前提
        settings.setBuiltInZoomControls(true);       //是否支持内置按钮缩放和手势“捏”缩放，如果设为false则webview不支持缩放功能
        settings.setDisplayZoomControls(false);      //是否隐藏原生的缩放控件
// 设置可以执行Javascript脚本
        settings.setJavaScriptEnabled(true);
// 设置缓存网页数据
        settings.setDomStorageEnabled(true);         //开启 DOM storage API 功能
        settings.setDatabaseEnabled(true);           //开启 database storage API 功能
        settings.setAppCacheEnabled(true);           //开启 Application Caches 功能
// 设置支持多窗口,要复写 WebChromeClient的onCreateWindow方法
        settings.setSupportMultipleWindows(true);    //支持多窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);  //支持js打开新窗口
// 支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
// 设置编码格式
        settings.setDefaultTextEncodingName("utf-8");
        if (url != null) {
            mWebView.loadUrl(url);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
               // SystemClock.sleep(1000);
                mFab.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initListener() {
        mToobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });

        mToobar.inflateMenu(R.menu.news_detail);
        mToobar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_web_view:
                     //   NewsBrowserActivity.startAction(NewsDetailActivity.this, mShareLink, mNewsTitle);
                        break;
                    case R.id.action_browser:
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        if (canBrowse(intent)) {
                            Uri uri = Uri.parse(mShareLink);
                            intent.setData(uri);
                            startActivity(intent);}
                            break;

                }
                return true;
            }


        });
    }

        private boolean canBrowse(Intent intent) {
            return intent.resolveActivity(getPackageManager()) != null && mShareLink != null;
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView!=null){
            mWebView=null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
    }
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
            //| View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}
