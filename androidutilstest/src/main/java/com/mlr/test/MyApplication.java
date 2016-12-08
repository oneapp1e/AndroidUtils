package com.mlr.test;

import android.app.Application;
import android.util.Log;

import com.mlr.widget.LoadingAndRetryManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Created by mulinrui on 12/5 0005.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                Log.e("testapp", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.e("testapp", " onCoreInitFinished  ");
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("testapp", "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("testapp", "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("testapp", "onDownloadProgress:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);

        LoadingAndRetryManager.BASE_RETRY_LAYOUT_ID = R.layout.offline_layout;
        LoadingAndRetryManager.BASE_LOADING_LAYOUT_ID = R.layout.base_loading;
        LoadingAndRetryManager.BASE_EMPTY_LAYOUT_ID = R.layout.no_content_layout;

    }
}
