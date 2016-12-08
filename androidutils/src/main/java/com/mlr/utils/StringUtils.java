/*
 * File Name: StringUtils.java 
 * History:
 * Created by wangyl on 2011-9-5
 */
package com.mlr.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 字符串工具类
 * 
 * @author wangyl
 * @version
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        return isEmpty(str, false);
    }

    public static boolean isEmpty(CharSequence str, boolean trim) {
        if (trim && str != null) {
            str = String.valueOf(str).trim();
        }
        return TextUtils.isEmpty(str) || "null".equals(str);
    }


    public static String getFormattedDateTime(long timestamp, String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            Date date = new Date(timestamp);
            return format.format(date.getTime());
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return "";
    }

}
