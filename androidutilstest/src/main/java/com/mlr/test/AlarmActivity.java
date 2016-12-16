package com.mlr.test;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;

import com.mlr.test.utils.AlarmClock;
import com.mlr.test.utils.AlarmUtil;
import com.mlr.utils.BaseActivity;
import com.mlr.utils.LogUtils;

import java.util.Calendar;

/**
 * Created by mulinrui on 12/6 0006.
 */
public class AlarmActivity extends BaseActivity {

    public static final String EXTRA = "AlarmActivity:button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        Button button1 = (Button) findViewById(R.id.button1);
        ViewCompat.setTransitionName(button1, EXTRA);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动定时器
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                LogUtils.e("开启闹钟的时间  hour:" + hour + "  minute:" + (minute + 1));
                AlarmClock alarmClock = new AlarmClock(hour, minute + 1);
                AlarmUtil.startAlarmClock(getActivity(), alarmClock);
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭定时器
                AlarmUtil.cancelAlarmClock(getActivity(), 0);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }
}
