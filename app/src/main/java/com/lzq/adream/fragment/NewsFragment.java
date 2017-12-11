package com.lzq.adream.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzq.adream.AppConstant;
import com.lzq.adream.AppUtils;
import com.lzq.adream.R;
import com.lzq.adream.adapter.NewListAdapter;
import com.lzq.adream.base.BaseFragment;
import com.lzq.adream.base.BaseResponse;
import com.lzq.adream.interfaces.NewsListContract;
import com.lzq.adream.model.bean.NewsList;
import com.lzq.adream.model.models.NewsListModel;
import com.lzq.adream.presenter.NewsListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${廖昭启} on 2017/11/7.
 */

public class NewsFragment extends BaseFragment<NewsListPresenter, NewsListModel> implements OnRefreshListener, OnLoadMoreListener, NewsListContract.View {
    private String mNewsId;
    private String mNewsType;
    private IRecyclerView mIRecyclerView;
    private NewListAdapter mNewListAdapter;
    private List<NewsList.NewslistBean> mNewsSummaryList = new ArrayList<>();
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
        if (getArguments() != null) {
            mNewsId = getArguments().getString(AppConstant.NEWS_ID);
            mNewsType = getArguments().getString(AppConstant.NEWS_TYPE);
        }

        mIRecyclerView = (IRecyclerView) findViewById(R.id.iRV_news);
        mIRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewsSummaryList.clear();
        mIRecyclerView.setOnRefreshListener(this);
        mIRecyclerView.setOnLoadMoreListener(this);
        mNewListAdapter = new NewListAdapter(getContext(), mNewsSummaryList);
        mNewListAdapter.openLoadAnimation(new ScaleInAnimation());
        mIRecyclerView.setAdapter(mNewListAdapter);
        if (mNewListAdapter.getSize() <= 0) {
            mStartPage = 0;
            mPresenter.getNewsListDataRequest(mNewsType, mNewsId, mStartPage);
            Log.e("ID", "id=====" + mNewsId);
        }


    }

    @Override
    public void onRefresh() {
        mNewListAdapter.getPageBean().setRefresh(true);
        mStartPage = 0;
        //发起请求
        mIRecyclerView.setRefreshing(true);
        mPresenter.getNewsListDataRequest(mNewsType, mNewsId, mStartPage);

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
    public void returnNewsListData(BaseResponse<String> newsSummaries) {
        List<NewsList.NewslistBean> newList = null;
        Log.e("头条Json", newsSummaries.getNewslist());
        if (newsSummaries != null) {
            Log.e("fragment", newsSummaries.toString());


            newList = new Gson().fromJson(newsSummaries.getNewslist(), new TypeToken<List<NewsList.NewslistBean>>() {
            }.getType());

            if (newList != null) {
                mNewListAdapter.addAll(newList);
            }
        }

        if (newList != null) {
            mStartPage += 20;
            if (mNewListAdapter.getPageBean().isRefresh()) {
                mIRecyclerView.setRefreshing(false);
                mNewListAdapter.addAll(newList);
            } else {
                if (newList.size() > 0) {
                    mIRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    mNewListAdapter.replaceAll(newList);
                } else {
                    mIRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    @Override
    public void scrolltoTop() {

    }

    @Override
    public void onLoadMore(View loadMoreView) {
        mNewListAdapter.getPageBean().setRefresh(false);
        //发起请求
        mIRecyclerView.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getNewsListDataRequest(mNewsType, mNewsId, mStartPage);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.hideBottomUIMenu(getContext());
    }

}
