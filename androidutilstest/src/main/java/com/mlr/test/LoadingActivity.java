package com.mlr.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mlr.utils.BaseActivity;
import com.mlr.utils.ToastUtils;
import com.mlr.widget.LoadingAndRetryManager;
import com.mlr.widget.OnLoadingAndRetryListener;


public class LoadingActivity extends BaseActivity {

    LoadingAndRetryManager mLoadingAndRetryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        mLoadingAndRetryManager = LoadingAndRetryManager.generate(this, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                setRetryAndEmpty(retryView);
            }

            @Override
            public void setEmptyEvent(View emptyView) {
                setRetryAndEmpty(emptyView);
            }
        });

        loadData();

    }

    private void loadData() {
        mLoadingAndRetryManager.showLoading();

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double v = Math.random();
                if (v > 0.8) {
                    mLoadingAndRetryManager.showContent();
                } else if (v > 0.4) {
                    mLoadingAndRetryManager.showRetry();
                } else {
                    mLoadingAndRetryManager.showEmpty();
                }
            }
        }.start();


    }


    public void setRetryAndEmpty(View retryView) {
        retryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastSafe(getActivity(), "retry event invoked", Toast.LENGTH_LONG);
                loadData();
            }
        });
    }
}
