package com.mlr.test.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mlr.utils.LogUtils;

/**
 * Created by mulinrui on 12/14 0014.
 */
public class AlarmClockBroadcast extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        //判断闹钟开关是否开启
        AlarmClock alarmClock = intent
                .getParcelableExtra(AlarmUtil.ALARM_CLOCK);
        if (alarmClock != null) {
            LogUtils.e("收到闹钟广播");

            // todo 需要判断是否开启第二天闹钟
            AlarmUtil.startAlarmClock(context, alarmClock);
        }else{
            LogUtils.e("不知道你是什么");
        }
    }
}
