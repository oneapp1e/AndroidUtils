package com.mlr.test.mvp.presenter.impl;

import com.mlr.test.mvp.View.BaseView;
import com.mlr.test.mvp.presenter.BasePresenter;

/**
 * Created by mulinrui on 12/16 0016.
 */
public class BasePresenterImpl<T extends BaseView> implements BasePresenter {

    protected T mView;


    public BasePresenterImpl(T view) {
        mView = view;
    }

}
