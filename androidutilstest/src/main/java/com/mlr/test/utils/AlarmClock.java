/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.mlr.test.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 闹钟实例
 *
 * @author 咖枯
 * @version 1.0 2015/06
 */
public class AlarmClock implements Parcelable {

    /**
     * 小时
     */
    private int hour;

    /**
     * 分钟
     */
    private int minute;

    /**
     * 闹钟实例构造方法
     *
     * @param hour   小时
     * @param minute 分钟
     */
    public AlarmClock(int hour, int minute) {
        super();
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(hour);
        out.writeInt(minute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private AlarmClock(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
    }

    public static final Parcelable.Creator<AlarmClock> CREATOR = new Creator<AlarmClock>() {

        @Override
        public AlarmClock createFromParcel(Parcel in) {
            return new AlarmClock(in);
        }

        @Override
        public AlarmClock[] newArray(int size) {

            return new AlarmClock[size];
        }
    };


    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

}
