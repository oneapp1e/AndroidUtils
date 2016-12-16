package com.mlr.test.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.mlr.utils.LogUtils;

import java.util.Calendar;

/**
 * Created by mulinrui on 12/14 0014.
 */
public class AlarmUtil {

    /**
     * 保存的AlarmClock单例
     */
    public static final String ALARM_CLOCK = "alarm_clock";

    /**
     * 开启闹钟
     *
     * @param context    context
     * @param alarmClock 闹钟实例
     */
    public static void startAlarmClock(Context context, AlarmClock alarmClock) {
        Intent intent = new Intent(context, AlarmClockBroadcast.class);
        intent.putExtra(ALARM_CLOCK, alarmClock);
        // FLAG_UPDATE_CURRENT：如果PendingIntent已经存在，保留它并且只替换它的extra数据。
        // FLAG_CANCEL_CURRENT：如果PendingIntent已经存在，那么当前的PendingIntent会取消掉，然后产生一个新的PendingIntent。
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        // 取得下次响铃时间
        long nextTime = calculateNextTime(alarmClock.getHour(),
                alarmClock.getMinute());
        LogUtils.e("开启闹钟时间:" + nextTime);
        // 设置闹钟
        // 当前版本为19（4.4）或以上使用精准闹钟
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
        }
    }

    /**
     * 取消闹钟
     *
     * @param context      context
     * @param alarmClockId 闹钟启动id
     */
    public static void cancelAlarmClock(Context context, int alarmClockId) {
        Intent intent = new Intent(context, AlarmClockBroadcast.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, alarmClockId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context
                .getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
    }


    /**
     * 取得下次响铃时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @return 下次响铃时间
     */
    public static long calculateNextTime(int hour, int minute) {
        // 当前系统时间
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 下次响铃时间
        long nextTime = calendar.getTimeInMillis();
        // 当单次响铃时
        // 当设置时间大于系统时间时
        if (nextTime > now) {
            return nextTime;
        } else {
            // 设置的时间加一天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            nextTime = calendar.getTimeInMillis();
            return nextTime;
        }
    }
}
