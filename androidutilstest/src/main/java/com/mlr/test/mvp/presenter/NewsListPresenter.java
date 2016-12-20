package com.mlr.test.mvp.presenter;

import com.mlr.test.mvp.entity.NewsSummary;

import java.util.List;

/**
 * Created by mulinrui on 12/16 0016.
 */

public interface NewsListPresenter extends BasePresenter {

    void refreshData();

    int loadMore(List<NewsSummary> out, int startPosition, int requestSize);

}
