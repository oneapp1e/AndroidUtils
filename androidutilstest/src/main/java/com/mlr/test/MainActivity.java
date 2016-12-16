package com.mlr.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mlr.utils.BaseActivity;
import com.mlr.utils.VersionUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Button btn01 = (Button) findViewById(R.id.btn01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), X5WebViewActivity.class);
                startActivity(intent);
            }
        });

        Button btn02 = (Button) findViewById(R.id.btn02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoadingActivity.class);
                startActivity(intent);
            }
        });

        Button btn03 = (Button) findViewById(R.id.btn03);
        btn03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Retrofit2Activity.class);
                startActivity(intent);
            }
        });

        final Button btn04 = (Button) findViewById(R.id.btn04);
        btn04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlarmActivity.class);
                if (VersionUtils.hasJellyBean()) {
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(mActivity, btn04, getString(R.string.extra));
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }

            }
        });
    }
}
