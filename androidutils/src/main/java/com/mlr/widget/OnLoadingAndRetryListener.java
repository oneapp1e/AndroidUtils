package com.mlr.widget;

import android.view.View;

public interface OnLoadingAndRetryListener {

    void setRetryEvent(View retryView);

    void setLoadingEvent(View loadingView);

    void setEmptyEvent(View emptyView);

    int generateLoadingLayoutId();

    int generateRetryLayoutId();

    int generateEmptyLayoutId();

    View generateLoadingLayout();

    View generateRetryLayout();

    View generateEmptyLayout();

}