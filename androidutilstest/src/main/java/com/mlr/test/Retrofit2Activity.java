package com.mlr.test;

import android.os.Bundle;
import android.view.View;

import com.mlr.mrecyclerview.MRecyclerView;
import com.mlr.test.mvp.View.NewsListView;
import com.mlr.test.mvp.adapter.NewsListAdapter;
import com.mlr.test.mvp.entity.NewsSummary;
import com.mlr.test.mvp.presenter.NewsListPresenter;
import com.mlr.test.mvp.presenter.impl.NewsListPresenterImpl;
import com.mlr.utils.BaseActivity;
import com.mlr.utils.LoadMoreListener;
import com.mlr.utils.LogUtils;

import java.util.List;

/**
 * Created by mulinrui on 12/6 0006.
 */
public class Retrofit2Activity extends BaseActivity implements NewsListView {

    private NewsListPresenter mNewsListPresenter;

    NewsListAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit2);

        initLoadingAndRetryManager();

        mNewsListPresenter = new NewsListPresenterImpl(this);


        MRecyclerView mRecyclerView = (MRecyclerView) findViewById(R.id.recyclerView);

        mRecyclerViewAdapter = new NewsListAdapter(getActivity(), null);
        mRecyclerViewAdapter.setLoadMoreListener(new LoadMoreListener<NewsSummary>() {
            @Override
            public int onLoadMoreRequested(List<NewsSummary> out, int startPosition, int requestSize) {
                return mNewsListPresenter.loadMore(out, startPosition, requestSize);
            }
        });
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        showProgress();
        mNewsListPresenter.refreshData();
    }


    @Override
    public void setNewList(List<NewsSummary> lists) {
        hideProgress();
        LogUtils.e("lists:"+lists.toString());
        mRecyclerViewAdapter.setData(lists);
    }


    public void setRetryAndEmpty(View retryView) {
        retryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();;
                mNewsListPresenter.refreshData();
            }
        });
    }
}
