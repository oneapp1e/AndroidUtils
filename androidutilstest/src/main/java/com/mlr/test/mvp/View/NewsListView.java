package com.mlr.test.mvp.View;

import com.mlr.test.mvp.entity.NewsSummary;

import java.util.List;

/**
 * Created by mulinrui on 12/16 0016.
 */
public interface NewsListView extends BaseView {

    /**
     * 设置头条新闻列表
     * @param lists
     */
    void setNewList(List<NewsSummary> lists);

}
