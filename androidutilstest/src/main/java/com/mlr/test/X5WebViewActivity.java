package com.mlr.test;

import android.os.Bundle;

import com.mlr.utils.BaseActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by mulinrui on 12/6 0006.
 */
public class X5WebViewActivity extends BaseActivity {

    // ==========================================================================
    // Constants
    // ==========================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://www.baidu.com/");
    }

    // ==========================================================================
    // Fields
    // ==========================================================================


    // ==========================================================================
    // Constructors
    // ==========================================================================


    // ==========================================================================
    // Getters
    // ==========================================================================


    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================


    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================

}
