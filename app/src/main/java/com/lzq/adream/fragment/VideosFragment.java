package com.lzq.adream.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzq.adream.AppConstant;
import com.lzq.adream.R;
import com.lzq.adream.base.BaseFragment;
import com.lzq.adream.interfaces.VideosListContract;
import com.lzq.adream.model.bean.VideoData;
import com.lzq.adream.model.models.VideoListModel;
import com.lzq.adream.presenter.VideoListPresenter;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by ${廖昭启} on 2017/11/10.
 */

public class VideosFragment extends BaseFragment<VideoListPresenter, VideoListModel> implements VideosListContract.View, OnRefreshListener, OnLoadMoreListener {

    private String mViodeoType;
    private IRecyclerView mIRviodeoList;
    private int mStartPage;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_news;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    protected void initView() {
        mIRviodeoList = (IRecyclerView) findViewById(R.id.iRV_news);
        mIRviodeoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mViodeoType = getActivity().getIntent().getStringExtra(AppConstant.VIDEO_TYPE);
        CommonRecycleViewAdapter<VideoData> commonRecycleViewAdapter = new CommonRecycleViewAdapter<VideoData>(getContext(), R.layout.item_video_list) {
            @Override
            public void convert(ViewHolderHelper helper, VideoData videoData) {
                helper.setImageRoundUrl(R.id.iv_logo, videoData.getTopicImg());
                helper.setText(R.id.tv_from, videoData.getTopicName());
                helper.setText(R.id.tv_play_time, String.format(getResources().getString(R.string.video_play_times), String.valueOf(videoData.getPlayCount())));
                JCVideoPlayerStandard jcVideoPlayerStandard = helper.getView(R.id.videoplayer);
                boolean setUp = jcVideoPlayerStandard.setUp(
                        videoData.getMp4_url(), JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        TextUtils.isEmpty(videoData.getDescription()) ? videoData.getTitle() + "" : videoData.getDescription());
                if (setUp) {
                    Glide.with(mContext).load(videoData.getCover())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .error(R.drawable.ic_empty_picture)
                            .crossFade().into(jcVideoPlayerStandard.thumbImageView);
                }
            }
        };

        mIRviodeoList.setAdapter(commonRecycleViewAdapter);
        mIRviodeoList.setOnRefreshListener(this);
        mIRviodeoList.setOnLoadMoreListener(this);
        mIRviodeoList.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (JCVideoPlayerManager.listener() != null) {
                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.listener();
                    if (((ViewGroup) v).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });
        //数据为空才重新发起请求
        if(commonRecycleViewAdapter.getSize()<=0) {
            //发起请求
            mStartPage=0;
            mPresenter.getVideosListDataRequest(mViodeoType, mStartPage);
        }
    }


    @Override
    public void returnVideosListData(List<VideoData> videoDatas) {

    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void onRefresh() {
        mStartPage+=1;
        mPresenter.getVideosListDataRequest(mViodeoType, mStartPage);
    }

    @Override
    public void onLoadMore(View loadMoreView) {

    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
