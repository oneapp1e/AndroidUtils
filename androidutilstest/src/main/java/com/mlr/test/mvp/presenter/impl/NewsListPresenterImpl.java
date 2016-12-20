package com.mlr.test.mvp.presenter.impl;

import com.mlr.test.mvp.View.NewsListView;
import com.mlr.test.mvp.entity.NewsSummary;
import com.mlr.test.mvp.presenter.NewsListPresenter;
import com.mlr.test.mvp.retrofit2.NewListService;
import com.mlr.test.mvp.retrofit2.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.start;

/**
 * Created by mulinrui on 12/16 0016.
 */
public class NewsListPresenterImpl extends BasePresenterImpl<NewsListView> implements NewsListPresenter {

    public NewsListPresenterImpl(NewsListView view) {
        super(view);
    }

    @Override
    public void refreshData() {
        //重新加载数据
        NewListService newListService = RetrofitBuilder.retrofit.create(NewListService.class);
        Call<List<NewsSummary>> call = newListService.getNewHotList(0, 20);
        call.enqueue(new Callback<List<NewsSummary>>() {
            @Override
            public void onResponse(Call<List<NewsSummary>> call, Response<List<NewsSummary>> response) {
                mView.setNewList(response.body());
            }

            @Override
            public void onFailure(Call<List<NewsSummary>> call, Throwable t) {

            }
        });
    }

    @Override
    public int loadMore(final List<NewsSummary> out, int startPosition, int requestSize) {
        //加载更多数据
        final int[] code = {-1};
        NewListService newListService = RetrofitBuilder.retrofit.create(NewListService.class);
        Call<List<NewsSummary>> call = newListService.getNewHotList(start, requestSize);
        call.enqueue(new Callback<List<NewsSummary>>() {
            @Override
            public void onResponse(Call<List<NewsSummary>> call, Response<List<NewsSummary>> response) {
                out.addAll(response.body());
                code[0] = response.code();
            }

            @Override
            public void onFailure(Call<List<NewsSummary>> call, Throwable t) {

            }
        });

        return code[0];
    }
}
